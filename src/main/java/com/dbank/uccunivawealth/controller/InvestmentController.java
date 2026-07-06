package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.InvestmentAccount;
import com.dbank.uccunivawealth.util.UiUtils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for {@code investment.fxml}: creating investment accounts, adding/withdrawing
 * funds, and simulating an annual return.
 */
public class InvestmentController {

    @FXML
    private TableView<InvestmentAccount> investmentTable;
    @FXML
    private TableColumn<InvestmentAccount, Double> balCol;

    @FXML
    private MFXTextField ownerField;
    @FXML
    private MFXTextField initialBalField;
    @FXML
    private MFXComboBox<String> typeBox;
    @FXML
    private MFXComboBox<String> riskBox;
    @FXML
    private MFXTextField returnField;
    @FXML
    private MFXTextField amountField;

    private final AppData appData = AppData.getInstance();

    @FXML
    public void initialize() {
        investmentTable.setItems(appData.getInvestmentAccounts());
        balCol.setCellFactory(col -> UiUtils.moneyCell());

        typeBox.setItems(FXCollections.observableArrayList(
                "Stocks", "Bonds", "Mutual Fund", "Treasury Bills", "Real Estate", "Fixed Deposit"));
        riskBox.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
    }

    @FXML
    private void onCreateAccount() {
        try {
            int userId = 1;
            String accountNo = "";

            String owner = UiUtils.requireNonEmpty(ownerField.getText(), "Owner name");
            double initialBal = UiUtils.parsePositiveOrZero(initialBalField.getText(), "Initial investment");
            String type = typeBox.getValue();
            String risk = riskBox.getValue();
            if (type == null) {
                throw new IllegalArgumentException("Please select an investment type.");
            }
            if (risk == null) {
                throw new IllegalArgumentException("Please select a risk level.");
            }
            double expReturn = UiUtils.parsePositiveOrZero(returnField.getText(), "Expected return") / 100.0;

            InvestmentAccount account = new InvestmentAccount(
                    userId, accountNo, owner, initialBal, type, expReturn, risk);
            appData.getInvestmentAccounts().add(account);
            appData.recordTransactionsOf(account);

            ownerField.clear();
            initialBalField.clear();
            typeBox.setValue(null);
            riskBox.setValue(null);
            returnField.clear();
            UiUtils.showInfo("Investment account " + account.getAccountNumber() + " created for " + owner + ".");
        } catch (Exception ex) {
            UiUtils.showError(ex.getMessage());
        }
    }

    @FXML
    private void onDeposit() {
        performAction("deposit");
    }

    @FXML
    private void onWithdraw() {
        performAction("withdraw");
    }

    @FXML
    private void onSimulateReturn() {
        InvestmentAccount selected = investmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtils.showError("Please select an investment account first.");
            return;
        }
        double gain = selected.simulateAnnualReturn();
        appData.recordLatestTransactionOf(selected);
        investmentTable.refresh();
        String sign = gain >= 0 ? "gain" : "loss";
        UiUtils.showInfo(String.format(
                "Simulated a %s of GHS %.2f on account %s.", sign, Math.abs(gain), selected.getAccountNumber()));
    }

    private void performAction(String action) {
        InvestmentAccount selected = investmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtils.showError("Please select an account first.");
            return;
        }
        try {
            double amount = UiUtils.parsePositiveOrZero(amountField.getText(), "Amount");
            if (amount <= 0) {
                throw new IllegalArgumentException("Enter an amount greater than zero.");
            }

            if (action.equals("deposit")) {
                selected.deposit(amount);
            } else {
                selected.withdraw(amount);
            }
            appData.recordLatestTransactionOf(selected);
            amountField.clear();
            investmentTable.refresh();
            UiUtils.showInfo(String.format("%s of GHS %.2f successful. New balance: GHS %.2f",
                    action.equals("deposit") ? "Deposit" : "Withdrawal", amount, selected.getBalance()));
        } catch (Exception ex) {
            UiUtils.showError(ex.getMessage());
        }
    }
}
