package com.dbank.uccunivawealth.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class StartController {

    @FXML private StackPane contentArea;

    @FXML private Button btnDashboard;
    @FXML private Button btnSavings;
    @FXML private Button btnInvestment;
    @FXML private Button btnTransactions;
    @FXML private Button btnGoals;

    private Node dashboardView;
    private Node savingsView;
    private Node investmentView;
    private Node transactionsView;
    private Node goalsView;

    @FXML
    public void initialize() throws IOException {

        dashboardView = load("/com/dbank/uccunivawealth/dashboard.fxml");
        savingsView = load("/com/dbank/uccunivawealth/savings.fxml");
        investmentView = load("/com/dbank/uccunivawealth/investment.fxml");
        transactionsView = load("/com/dbank/uccunivawealth/transactions.fxml");
        goalsView = load("/com/dbank/uccunivawealth/goals.fxml");

        showView(dashboardView);
        setActive(btnDashboard);
    }

    private Node load(String fxml) throws IOException {
        return FXMLLoader.load(getClass().getResource(fxml));
    }

    private void showView(Node newView) {

        if (!contentArea.getChildren().isEmpty()) {
            Node oldView = contentArea.getChildren().get(0);

            // Fade out old view
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), oldView);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            // Slide in new view
            newView.setTranslateX(50);
            newView.setOpacity(0);

            contentArea.getChildren().add(newView);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), newView);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(200), newView);
            slideIn.setFromX(50);
            slideIn.setToX(0);

            ParallelTransition show = new ParallelTransition(fadeIn, slideIn);

            fadeOut.setOnFinished(e -> {
                contentArea.getChildren().remove(oldView);
                show.play();
            });

            fadeOut.play();
        } else {
            contentArea.getChildren().setAll(newView);
        }
    }

    private void setActive(Button active) {

        Button[] buttons = {
                btnDashboard, btnSavings, btnInvestment,
                btnTransactions, btnGoals
        };

        for (Button b : buttons) {
            b.getStyleClass().remove("active");
        }

        active.getStyleClass().add("active");
    }

    // NAVIGATION METHODS
    @FXML public void showDashboard() {
        showView(dashboardView);
        setActive(btnDashboard);
    }

    @FXML public void showSavings() {
        showView(savingsView);
        setActive(btnSavings);
    }

    @FXML public void showInvestment() {
        showView(investmentView);
        setActive(btnInvestment);
    }

    @FXML public void showTransactions() {
        showView(transactionsView);
        setActive(btnTransactions);
    }

    @FXML public void showGoals() {
        showView(goalsView);
        setActive(btnGoals);
    }
}