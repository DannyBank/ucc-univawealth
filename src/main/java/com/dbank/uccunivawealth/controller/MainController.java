package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.service.AuthService;
import com.dbank.uccunivawealth.service.LoggerService;
import com.dbank.uccunivawealth.service.UserSession;
import com.dbank.uccunivawealth.util.Notification;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
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

    private Node dashboardView;
    private Node savingsView;
    private Node investmentView;
    private Node transactionsView;
    private Node goalsView;

    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        try {
            AppData.getInstance().loadAllData();

            FXMLLoader dashboardLoader = new FXMLLoader(
                    getClass().getResource("/com/dbank/uccunivawealth/dashboard.fxml")
            );
            dashboardView = dashboardLoader.load();
            dashboardController = dashboardLoader.getController();

            savingsView = loadView("/com/dbank/uccunivawealth/savings.fxml");
            investmentView = loadView("/com/dbank/uccunivawealth/investment.fxml");
            transactionsView = loadView("/com/dbank/uccunivawealth/transactions.fxml");
            goalsView = loadView("/com/dbank/uccunivawealth/goals.fxml");

            showDashboard();
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }

    private Node loadView(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        return loader.load();
    }

    private void showView(Node newView) {
        if (newView == null) return;

        if (contentArea.getChildren().isEmpty()) {
            contentArea.getChildren().setAll(newView);
            return;
        }

        Node oldView = contentArea.getChildren().get(0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), oldView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        newView.setOpacity(0);
        newView.setTranslateX(35);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(220), newView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(220), newView);
        slideIn.setFromX(35);
        slideIn.setToX(0);

        ParallelTransition showAnimation = new ParallelTransition(fadeIn, slideIn);

        fadeOut.setOnFinished(e -> {
            contentArea.getChildren().setAll(newView);
            showAnimation.play();
        });

        fadeOut.play();
    }

    private void setActive(Button activeButton) {
        Button[] buttons = { btnDashboard, btnSavings, btnInvestment, btnTransactions, btnGoals };

        for (Button button : buttons) {
            if (button != null) {
                button.getStyleClass().remove("active");
            }
        }

        if (activeButton != null && !activeButton.getStyleClass().contains("active")) {
            activeButton.getStyleClass().add("active");
        }
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

    public void refreshDashboard() {
        if (dashboardController != null) {
            dashboardController.refreshDashboard();
        }
    }

    @FXML
    public void logout() throws IOException {

        boolean confirmed = Notification.showConfirmation(
                "Logout",
                "Confirm Logout",
                "Are you sure you want to end session?"
        );

        if (confirmed) {
            if (UserSession.getInstance().getCurrentUser() != null) {
                int userId = UserSession.getInstance().getCurrentUser().getUserId();
                new AuthService().sessionLogout(userId);
            }

            Notification.showInfo("See you again soon");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dbank/uccunivawealth/login-view.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(root, 550, 380));
            stage.centerOnScreen();
            stage.show();
        } else {
            System.out.println("Logout Cancelled.");
        }
    }

    public void addSavings(ActionEvent actionEvent) {
        showSavings();
    }

    public void addInvestment(ActionEvent actionEvent) {
        showInvestment();
    }
}