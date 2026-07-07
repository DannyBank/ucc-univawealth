package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.SavingsAccount;
import com.dbank.uccunivawealth.service.UserSession;
import com.dbank.uccunivawealth.util.Notification;
import com.dbank.uccunivawealth.util.UiUtils;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

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
    @FXML
    private MFXTextField targetAmtField;
    @FXML
    private MFXDatePicker targetDateField;

    private final AppData appData = AppData.getInstance();
    private final int userId = UserSession.getInstance().getCurrentUser().getUserId();

    @FXML
    public void initialize() {
        appData.loadSavingsAccounts(userId);
        savingsTable.setItems(appData.getSavingsAccounts());
        balCol.setCellFactory(col -> UiUtils.moneyCell());
    }

    @FXML
    private void onCreateAccount() {
        try {
            String accountNo = generateAccountNumber();
            String owner = UiUtils.requireNonEmpty(ownerField.getText(), "Owner name");
            double initialBal = UiUtils.parsePositiveOrZero(initialBalField.getText(), "Initial deposit");
            double rate = UiUtils.parsePositiveOrZero(rateField.getText(), "Interest rate") / 100.0;
            double targetAmount = UiUtils.parsePositiveOrZero(targetAmtField.getText(), "Target Amount");
            String targetDate = UiUtils.requireNonEmpty(targetDateField.getText(), "Target Date");

            SavingsAccount account = new SavingsAccount(
                    userId, accountNo, owner, initialBal, rate,
                    targetAmount, 0, new Date().toString(),
                    targetDate, SavingsStatus.ACTIVE.toString()
            );
            if (appData.addSavingsAccount(account))
                appData.recordTransactionsOf(account);

            // clear all inputs
            clearFields();

            // show successful account creation
            Notification.showInfo("Congratulations! \nYour savings account " + account.getAccountNumber() + " has been created");
        } catch (Exception ex) {
            Notification.showError(ex.getMessage());
        }
    }

    private void clearFields() {
        ownerField.clear();
        initialBalField.clear();
        rateField.clear();
        targetDateField.clear();
        targetAmtField.clear();
    }

    public static String generateAccountNumber() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int random = new SecureRandom().nextInt(10000);
        return "UW" + timestamp + String.format("%04d", random);
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
        appData.recordLatestTransactionOf(selected);
        savingsTable.refresh();
        Notification.showInfo(String.format(
                "Applied interest of GHS %.2f to account %s.", interest, selected.getAccountNumber()));
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
            appData.recordLatestTransactionOf(selected);
            amountField.clear();
            savingsTable.refresh();
            Notification.showInfo(String.format("%s of GHS %.2f successful. New balance: GHS %.2f",
                    action.equals("deposit") ? "Deposit" : "Withdrawal", amount, selected.getBalance()));
        } catch (Exception ex) {
            Notification.showError(ex.getMessage());
        }
    }
}

enum SavingsStatus { ACTIVE, SUCCESS, INACTIVE }