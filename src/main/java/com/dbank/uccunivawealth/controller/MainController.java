package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AuthService;
import com.dbank.uccunivawealth.service.UserSession;
import com.dbank.uccunivawealth.util.Notification;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class MainController {

    @FXML private StackPane contentArea;
    @FXML private Button btnDashboard;
    @FXML private Button btnSavings;
    @FXML private Button btnInvestment;
    @FXML private Button btnTransactions;
    @FXML private Button btnGoals;
    @FXML private Button btnLogout;

    // Views
    private Node dashboardView;
    private Node savingsView;
    private Node investmentView;
    private Node transactionsView;
    private Node goalsView;

    // Controllers
    private DashboardController dashboardController;

    @FXML
    public void initialize() {

        try {

            // Dashboard (need controller)
            FXMLLoader dashboardLoader =
                    new FXMLLoader(getClass().getResource("/com/dbank/uccunivawealth/dashboard.fxml"));

            dashboardView = dashboardLoader.load();
            dashboardController = dashboardLoader.getController();

            // Other pages
            savingsView = load("/com/dbank/uccunivawealth/savings.fxml");
            investmentView = load("/com/dbank/uccunivawealth/investment.fxml");
            transactionsView = load("/com/dbank/uccunivawealth/transactions.fxml");
            goalsView = load("/com/dbank/uccunivawealth/goals.fxml");

            // Show dashboard by default
            showDashboard();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Node load(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        return loader.load();
    }

    private void showView(Node newView) {

        if (contentArea.getChildren().isEmpty()) {
            contentArea.getChildren().setAll(newView);
            return;
        }

        Node oldView = contentArea.getChildren().get(0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), oldView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        newView.setOpacity(0);
        newView.setTranslateX(40);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(220), newView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideIn =
                new TranslateTransition(Duration.millis(220), newView);

        slideIn.setFromX(40);
        slideIn.setToX(0);

        ParallelTransition showAnimation =
                new ParallelTransition(fadeIn, slideIn);

        fadeOut.setOnFinished(e -> {
            contentArea.getChildren().setAll(newView);
            showAnimation.play();
        });

        fadeOut.play();
    }

    private void setActive(Button activeButton) {

        Button[] buttons = {
                btnDashboard,
                btnSavings,
                btnInvestment,
                btnTransactions,
                btnGoals
        };

        for (Button button : buttons) {
            button.getStyleClass().remove("active");
        }

        activeButton.getStyleClass().add("active");
    }

    @FXML
    private void showDashboard() {

        if (dashboardController != null) {
            dashboardController.refreshDashboard();
        }

        showView(dashboardView);
        setActive(btnDashboard);
    }

    @FXML
    private void showSavings() {
        showView(savingsView);
        setActive(btnSavings);
    }

    @FXML
    private void showInvestment() {
        showView(investmentView);
        setActive(btnInvestment);
    }

    @FXML
    private void showTransactions() {
        showView(transactionsView);
        setActive(btnTransactions);
    }

    @FXML
    private void showGoals() {
        showView(goalsView);
        setActive(btnGoals);
    }

    /**
     * Allows MainApp or other classes to refresh the dashboard.
     */
    public void refreshDashboard() {

        if (dashboardController != null) {
            dashboardController.refreshDashboard();
        }
    }

    @FXML
    public void logout() throws IOException {

        // close the existing user session
        int userId = UserSession.getInstance().getCurrentUser().getUserId();
        new AuthService().sessionLogout(userId);

        // show goodbye message
        Notification.ShowOptions(Alert.AlertType.INFORMATION, "Logout", "Leaving", "See you again");

        // go back to login page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbank/uccunivawealth/login-view.fxml"));
        Parent root = loader.load();

        LoginController login = loader.getController();

        Stage stage = (Stage) btnGoals.getScene().getWindow();
        stage.setScene(new Scene(root, 450, 300));
        stage.centerOnScreen();
        stage.show();
    }
}