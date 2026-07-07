package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.SavingsGoal;
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

    @FXML
    public void initialize() {
        renderGoals();
    }

    @FXML
    private void onAddGoal() {
        try {
            String name = UiUtils.requireNonEmpty(nameField.getText(), "Goal name");
            double target = UiUtils.parsePositiveOrZero(targetField.getText(), "Target amount");
            if (target <= 0) {
                throw new IllegalArgumentException("Target amount must be greater than zero.");
            }
            double current = UiUtils.parsePositiveOrZero(currentField.getText(), "Current savings");
            String date = dateField.getText() == null || dateField.getText().isBlank()
                    ? "No date set"
                    : dateField.getText().trim();

            appData.getGoals().add(new SavingsGoal(name, target, current, date));

            nameField.clear();
            targetField.clear();
            currentField.clear();
            dateField.clear();
            renderGoals();
        } catch (Exception ex) {
            Notification.showError(ex.getMessage());
        }
    }

    private void renderGoals() {
        goalsList.getChildren().clear();

        if (appData.getGoals().isEmpty()) {
            goalsList.getChildren().add(new Label("No savings goals yet. Add one below."));
            return;
        }

        for (SavingsGoal goal : appData.getGoals()) {
            goalsList.getChildren().add(buildGoalCard(goal));
        }
    }

    private VBox buildGoalCard(SavingsGoal goal) {
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
    }
}
