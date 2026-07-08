package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.repo.SavingsGoalsRepository;
import com.dbank.uccunivawealth.repo.TransactionsRepository;
import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.SavingsGoal;
import com.dbank.uccunivawealth.service.LoggerService;
import com.dbank.uccunivawealth.service.UserSession;
import com.dbank.uccunivawealth.util.Notification;
import com.dbank.uccunivawealth.util.UiUtils;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller for {@code goals.fxml}. Each {@link SavingsGoal} is rendered as its own card
 * (name, progress bar, progress text) built up in code, since the number of goals is
 * dynamic and not something FXML alone can express.
 */
public class GoalsController {

    @FXML
    private VBox goalsList;

    @FXML
    private MFXTextField nameField;
    @FXML
    private MFXTextField targetField;
    @FXML
    private MFXTextField currentField;
    @FXML
    private MFXTextField dateField;

    private final AppData appData = AppData.getInstance();
    private final User currentUser = UserSession.getInstance().getCurrentUser();

    @FXML
    public void initialize() {
        appData.loadGoals();
        renderGoals();
    }

    @FXML
    private void onAddGoal() {
        try {
            int userId = currentUser.getUserId();
            String name = UiUtils.requireNonEmpty(nameField.getText(), "Goal name");
            double target = UiUtils.parsePositiveOrZero(targetField.getText(), "Target amount");
            if (target <= 0) {
                throw new IllegalArgumentException("Target amount must be greater than zero.");
            }
            double current = UiUtils.parsePositiveOrZero(currentField.getText(), "Current savings");
            String date = dateField.getText() == null || dateField.getText().isBlank()
                    ? "No date set" : dateField.getText().trim();

            SavingsGoal goal = new SavingsGoal(0, userId, name, target, current, date, "ACTIVE");
            if (!(new SavingsGoalsRepository().insert(goal) == 1))
                Notification.showError("An error occurred, please try again");

            appData.getGoals().add(goal);
            recordTransaction(userId, target, date, goal.getName());
            clearFields();
            renderGoals();

        } catch (Exception ex) {
            LoggerService.log(ex);
        }
    }

    private void recordTransaction(int userId, double target, String date, String goal) {
        new TransactionsRepository().insert(
                new Transaction(0, userId, 0, 0, "GOAL",
                        target, date, 8, goal));
    }

    private void clearFields() {
        try {
            nameField.clear();
            targetField.clear();
            currentField.clear();
            dateField.clear();
        } catch (Exception ex) {
            LoggerService.log(ex);
        }
    }

    private void renderGoals() {
        try {
            goalsList.getChildren().clear();

            if (appData.getGoals().isEmpty()) {
                goalsList.getChildren().add(new Label("No savings goals yet. Add one below."));
                return;
            }

            for (SavingsGoal goal : appData.getGoals()) {
                goalsList.getChildren().add(buildGoalCard(goal));
            }
        } catch (Exception ex) {
            LoggerService.log(ex);
        }
    }

    private VBox buildGoalCard(SavingsGoal goal) {
        try {
            Label nameLabel = new Label(goal.getName() + "  (target: " + goal.getTargetDate() + ")");
            nameLabel.getStyleClass().add("goal-name");

            MFXProgressBar bar = new MFXProgressBar();
            bar.setProgress(goal.getProgressFraction());
            bar.setPrefWidth(400);

            Label progressLabel = new Label(String.format("GHS %.2f / GHS %.2f  (%s)",
                    goal.getCurrentAmount(), goal.getTargetAmount(), goal.getProgressDisplay()));

            VBox card = new VBox(6, nameLabel, bar, progressLabel);
            card.getStyleClass().add("goal-card");
            return card;
        } catch (Exception ex) {
            LoggerService.log(ex);
            return null;
        }
    }
}