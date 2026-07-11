package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.Investment;
import com.dbank.uccunivawealth.service.LoggerService;
import com.dbank.uccunivawealth.service.UserSession;
import com.dbank.uccunivawealth.util.UiUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.Objects;

/**
 * Controller for {@code dashboard.fxml}. Shows the totals across all accounts and an
 * asset-allocation pie chart, refreshed either on request or when the Dashboard tab
 * regains focus (see {@link MainController}).
 */
public class DashboardController {

    public Label lblUserWelcome;
    @FXML
    private PieChart allocationChart;

    private final AppData appData = AppData.getInstance();

    @FXML
    public void initialize() {
        refreshDashboard();
    }

    @FXML
    private void onRefresh() {
        refreshDashboard();
    }

    @FXML private Label lblSavings;
    @FXML private Label lblInvestments;
    @FXML private Label lblNetWorth;

    @FXML
    private void addSavings() {
        // logic to add savings
    }

    @FXML
    private void addInvestment() {
        // logic to add investment
    }

    @FXML
    private void viewTransactions() {
        // logic to show transactions tab
    }

    public void refreshDashboard() {
        try {
            AppData.getInstance().clearAllData();
            User user = UserSession.getInstance().getCurrentUser();

            AppData.getInstance().loadAllData();
            int userId = user.getUserId();

            //greetings
            lblUserWelcome.setText("Welcome " + user.getUsername() + ", Overview of your financials below");

            double totalSavings = appData.getSavingsAccounts(userId).stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(item -> Objects.requireNonNullElse(item.getCurrentBalance(), 0.0))
                    .sum();
            double totalInvestment = appData.getInvestmentAccounts(userId).stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(item -> Objects.requireNonNullElse(item.getBalance(), 0.0))
                    .sum();
            double netWorth = totalSavings + totalInvestment;

            allocationChart.getData().clear();
            allocationChart.getData().add(new PieChart.Data("Savings", totalSavings));
            allocationChart.getData().add(new PieChart.Data("Investments", totalInvestment));

            lblSavings.setText(UiUtils.formatMoney(totalSavings));
            lblInvestments.setText(UiUtils.formatMoney(totalInvestment));
            lblNetWorth.setText(UiUtils.formatMoney(netWorth));

            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            if (totalSavings > 0) {
                pieData.add(new PieChart.Data("Savings", totalSavings));
            }
            for (Investment inv : appData.getInvestmentAccounts(user.getUserId())) {
                if (inv.getBalance() > 0) {
                    pieData.add(new PieChart.Data(
                            inv.getInvestmentType(), inv.getBalance()));
                }
            }
            if (pieData.isEmpty()) {
                pieData.add(new PieChart.Data("No funds yet", 1));
            }
            allocationChart.setData(pieData);
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }
}
