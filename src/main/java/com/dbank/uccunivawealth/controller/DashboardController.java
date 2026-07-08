package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.Investment;
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
        lblSavings.setText("GHS 5000.00");
        lblInvestments.setText("GHS 12000.00");
        lblNetWorth.setText("GHS 17000.00");

        allocationChart.getData().clear();
        allocationChart.getData().add(new PieChart.Data("Savings", 5000));
        allocationChart.getData().add(new PieChart.Data("Investments", 12000));

        double totalSavings = appData.getSavingsAccounts().stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> Objects.requireNonNullElse(item.getBalance(), 0.0))
                .sum();
        double totalInvestment = appData.getInvestmentAccounts().stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> Objects.requireNonNullElse(item.getBalance(), 0.0))
                .sum();
        double netWorth = totalSavings + totalInvestment;

        lblSavings.setText(UiUtils.formatMoney(totalSavings));
        lblInvestments.setText(UiUtils.formatMoney(totalInvestment));
        lblNetWorth.setText(UiUtils.formatMoney(netWorth));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        if (totalSavings > 0) {
            pieData.add(new PieChart.Data("Savings", totalSavings));
        }
        for (Investment inv : appData.getInvestmentAccounts()) {
            if (inv.getBalance() > 0) {
                pieData.add(new PieChart.Data(
                        inv.getInvestmentType(), inv.getBalance()));
            }
        }
        if (pieData.isEmpty()) {
            pieData.add(new PieChart.Data("No funds yet", 1));
        }
        allocationChart.setData(pieData);
    }
}
