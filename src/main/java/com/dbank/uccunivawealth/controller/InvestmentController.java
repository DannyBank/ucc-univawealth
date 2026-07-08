package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.Investment;
import com.dbank.uccunivawealth.service.LoggerService;
import com.dbank.uccunivawealth.service.UserSession;
import com.dbank.uccunivawealth.util.Notification;
import com.dbank.uccunivawealth.util.UiUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

/**
 * Controller for {@code investment.fxml}: creating investment accounts, adding/withdrawing
 * funds, and simulating an annual return.
 */
public class InvestmentController {

    public MFXTextField investmentNameField;
    public MFXTextField interestField;
    public MFXTextField durationField;
    public DatePicker startDateField;
    public DatePicker maturityDateField;
    public MFXTextField expectedReturnField;
    public MFXButton createBtn;
    public MFXTextField simAmountField;
    public MFXButton simReturnsBtn;
    public MFXButton depositBtn;
    @FXML
    private TableView<Investment> investmentTable;

    @FXML
    private MFXTextField initialBalField;
    @FXML
    private MFXComboBox<String> typeBox;
    @FXML private MFXComboBox<String> riskLevel;

    private final AppData appData = AppData.getInstance();
    private final User currentUser = UserSession.getInstance().getCurrentUser();

    @FXML
    public void initialize() {
        investmentTable.setItems(appData.getInvestmentAccounts());

        typeBox.setItems(FXCollections.observableArrayList(
                "Stocks", "Bonds", "Mutual Fund",
                "Treasury Bills", "Real Estate", "Fixed Deposit"));
        riskLevel.setItems(FXCollections.observableArrayList(
                "Low", "Growth", "Aggressive"));
    }

    @FXML
    private void onCreateAccount() {
        try {
            int userId = currentUser.getUserId();
            String accountNo = currentUser.getAccountNumber();

            String investmentName = investmentNameField.getText();
            double interest = UiUtils.parsePositiveOrZero(interestField.getText(), "Interest Rate");
            int durationMonths = Integer.parseInt(durationField.getText());
            String startDate = startDateField.getValue().toString();
            String maturityDate = maturityDateField.getValue().toString();
            double expectedReturn = UiUtils.parsePositiveOrZero(expectedReturnField.getText(), "Expected Return");
            double initialBal = UiUtils.parsePositiveOrZero(initialBalField.getText(), "Initial investment");
            String type = typeBox.getValue();

            if (type == null) {
                throw new IllegalArgumentException("Please select an investment type.");
            }

            Investment account = new Investment(0, userId,
                    investmentName, type, initialBal, interest,
                    durationMonths, startDate, maturityDate,
                    expectedReturn, "ACTIVE"
            );

            if (appData.addInvestmentAccount(account))
                clearFields();

            Notification.showInfo("Investment account was successfully created");
        } catch (Exception ex) {
            Notification.showError("An error occurred, Please try again");
            LoggerService.logError(ex);
        }
    }

    private void clearFields() {
        investmentNameField.clear();
        interestField.clear();
        durationField.clear();
        startDateField.setValue(null);
        maturityDateField.setValue(null);
        expectedReturnField.clear();
        simAmountField.clear();
        initialBalField.clear();
        typeBox.setValue(null);
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
        Investment selected = investmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Notification.showError("Please select an investment account first.");
            return;
        }
        double gain = simulateAnnualReturn(riskLevel.getValue());
        investmentTable.refresh();
        String sign = gain >= 0 ? "gain" : "loss";
        Notification.showInfo(String.format(
                "Simulated a %s of GHS %.2f.", sign, Math.abs(gain)));
    }

    public double simulateAnnualReturn(String riskLevel) {
        double varianceFactor = switch (riskLevel == null ? "" : riskLevel) {
            case "Low" -> 0.02;
            case "Medium" -> 0.08;
            case "High" -> 0.18;
            default -> 0.05;
        };

        return varianceFactor;
    }

    private void performAction(String action) {
        Investment selected = investmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Notification.showError("Please select an account first.");
            return;
        }
        try {
            double amount = UiUtils.parsePositiveOrZero(simAmountField.getText(), "Amount");
            if (amount <= 0) {
                throw new IllegalArgumentException("Enter an amount greater than zero.");
            }

            if (action.equals("deposit")) {
                deposit(amount);
            } else {
                withdraw(amount);
            }

            simAmountField.clear();
            investmentTable.refresh();

            Notification.showInfo(String.format("%s of GHS %.2f successful. New balance: GHS %.2f",
                    action.equals("deposit") ? "Deposit" : "Withdrawal", amount, selected.getBalance()));
        } catch (Exception ex) {
            Notification.showError(ex.getMessage());
        }
    }

    private void withdraw(double amount) {

    }

    private void deposit(double amount) {

    }
}
