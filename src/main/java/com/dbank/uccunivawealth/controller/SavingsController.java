package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.SavingsStatus;
import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.repo.TransactionsRepository;
import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.SavingsAccount;
import com.dbank.uccunivawealth.service.LoggerService;
import com.dbank.uccunivawealth.service.UserSession;
import com.dbank.uccunivawealth.util.Notification;
import com.dbank.uccunivawealth.util.UiUtils;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.Date;

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
    private MFXTextField initialBalField;
    @FXML
    private MFXTextField rateField;
    @FXML
    private MFXTextField amountField;
    @FXML
    private MFXTextField targetAmtField;
    @FXML
    private DatePicker targetDateField;

    private final AppData appData = AppData.getInstance();
    private final User currentUser = UserSession.getInstance().getCurrentUser();

    @FXML
    public void initialize() {
        appData.loadSavingsAccounts(currentUser.getUserId());
        savingsTable.setItems(appData.getSavingsAccounts());
        balCol.setCellFactory(col -> UiUtils.moneyCell());
    }

    @FXML
    private void onCreateAccount() {
        try {
            String accountNo = currentUser.getAccountNumber();
            int userId = currentUser.getUserId();

            double initialBal = UiUtils.parsePositiveOrZero(initialBalField.getText(), "Initial deposit");
            double rate = UiUtils.parsePositiveOrZero(rateField.getText(), "Interest rate") / 100.0;
            double targetAmount = UiUtils.parsePositiveOrZero(targetAmtField.getText(), "Target Amount");
            String targetDate = UiUtils.requireNonEmpty(targetDateField.getValue().toString(), "Target Date");

            SavingsAccount account = new SavingsAccount(
                    userId, accountNo, initialBal, rate,
                    targetAmount, initialBal, new Date().toString(),
                    targetDate, SavingsStatus.ACTIVE.toString()
            );
            if (!appData.addSavingsAccount(account))
                Notification.showError("An error occurred, please try again");

            recordTransaction(userId, initialBal, account.getStartDate(), "SAVINGS");

            // clear all inputs
            clearFields();

            // show successful account creation
            Notification.showInfo("Congratulations! \nYour savings account has been created");
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }

    private void recordTransaction(int userId, double target, String date, String goal){
        new TransactionsRepository().insert(
                new Transaction(0, userId, 0, 0, "SAVINGS",
                        target, date, 9, goal));
    }

    private void clearFields() {
        initialBalField.clear();
        rateField.clear();
        targetDateField.setValue(null);
        targetAmtField.clear();
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
            Notification.showError("Please select a savings account first.");
            return;
        }
        double interest = selected.applyMonthlyInterest();
        savingsTable.refresh();
        Notification.showInfo(String.format(
                "Applied interest of GHS %.2f", interest));
    }

    private void performAction(String action) {
        SavingsAccount selected = savingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Notification.showError("Please select an account first.");
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
            amountField.clear();
            savingsTable.refresh();
            Notification.showInfo(String.format("%s of GHS %.2f successful. New balance: GHS %.2f",
                    action.equals("deposit") ? "Deposit" : "Withdrawal", amount, selected.getBalance()));
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }
}