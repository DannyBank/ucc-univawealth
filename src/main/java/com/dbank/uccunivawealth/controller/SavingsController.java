package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.SavingsAccount;
import com.dbank.uccunivawealth.util.UiUtils;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for {@code savings.fxml}: creating savings accounts, depositing, withdrawing,
 * and applying monthly interest.
 */
public class SavingsController {

    @FXML
    private TableView<SavingsAccount> savingsTable;
    @FXML
    private TableColumn<SavingsAccount, Double> balCol;

    @FXML
    private MFXTextField ownerField;
    @FXML
    private MFXTextField initialBalField;
    @FXML
    private MFXTextField rateField;
    @FXML
    private MFXTextField amountField;

    private final AppData appData = AppData.getInstance();

    @FXML
    public void initialize() {
        savingsTable.setItems(appData.getSavingsAccounts());
        balCol.setCellFactory(col -> UiUtils.moneyCell());
    }

    @FXML
    private void onCreateAccount() {
        try {
            String owner = UiUtils.requireNonEmpty(ownerField.getText(), "Owner name");
            double initialBal = UiUtils.parsePositiveOrZero(initialBalField.getText(), "Initial deposit");
            double rate = UiUtils.parsePositiveOrZero(rateField.getText(), "Interest rate") / 100.0;

            SavingsAccount account = new SavingsAccount(appData.nextAccountId("SAV"), owner, initialBal, rate);
            appData.getSavingsAccounts().add(account);
            appData.recordTransactionsOf(account);

            ownerField.clear();
            initialBalField.clear();
            rateField.clear();
            UiUtils.showInfo("Savings account " + account.getAccountNumber() + " created for " + owner + ".");
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
    private void onApplyInterest() {
        SavingsAccount selected = savingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiUtils.showError("Please select a savings account first.");
            return;
        }
        double interest = selected.applyMonthlyInterest();
        appData.recordLatestTransactionOf(selected);
        savingsTable.refresh();
        UiUtils.showInfo(String.format(
                "Applied interest of GHS %.2f to account %s.", interest, selected.getAccountNumber()));
    }

    private void performAction(String action) {
        SavingsAccount selected = savingsTable.getSelectionModel().getSelectedItem();
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
            savingsTable.refresh();
            UiUtils.showInfo(String.format("%s of GHS %.2f successful. New balance: GHS %.2f",
                    action.equals("deposit") ? "Deposit" : "Withdrawal", amount, selected.getBalance()));
        } catch (Exception ex) {
            UiUtils.showError(ex.getMessage());
        }
    }
}
