package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.Account;
import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.InvestmentAccount;
import com.dbank.uccunivawealth.model.SavingsAccount;
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
    private Label totalSavingsLabel;
    @FXML
    private Label totalInvestmentLabel;
    @FXML
    private Label netWorthLabel;
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

    public void refreshDashboard() {
        double totalSavings = appData.getSavingsAccounts().stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> Objects.requireNonNullElse(item.getBalance(), 0.0))
                .sum();
        double totalInvestment = appData.getInvestmentAccounts().stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> Objects.requireNonNullElse(item.getBalance(), 0.0))
                .sum();
        double netWorth = totalSavings + totalInvestment;

        totalSavingsLabel.setText(UiUtils.formatMoney(totalSavings));
        totalInvestmentLabel.setText(UiUtils.formatMoney(totalInvestment));
        netWorthLabel.setText(UiUtils.formatMoney(netWorth));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        if (totalSavings > 0) {
            pieData.add(new PieChart.Data("Savings", totalSavings));
        }
        for (InvestmentAccount inv : appData.getInvestmentAccounts()) {
            if (inv.getBalance() > 0) {
                pieData.add(new PieChart.Data(
                        inv.getInvestmentType() + " (" + inv.getAccountNumber() + ")", inv.getBalance()));
            }
        }
        if (pieData.isEmpty()) {
            pieData.add(new PieChart.Data("No funds yet", 1));
        }
        allocationChart.setData(pieData);
    }
}
