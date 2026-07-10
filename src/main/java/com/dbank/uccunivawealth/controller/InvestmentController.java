package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.repo.TransactionsRepository;
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
import java.time.LocalDateTime;

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

    @FXML
    public void initialize() {
        try {
            User currentUser = UserSession.getInstance().getCurrentUser();
            investmentTable.setItems(appData.getInvestmentAccounts(currentUser.getUserId()));

            typeBox.setItems(FXCollections.observableArrayList(
                    "Stocks", "Bonds", "Mutual Fund",
                    "Treasury Bills", "Real Estate", "Fixed Deposit"));
            riskLevel.setItems(FXCollections.observableArrayList(
                    "Low", "Growth", "Aggressive"));
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }

    @FXML
    private void onCreateAccount() {
        try {
            User currentUser = UserSession.getInstance().getCurrentUser();

            int userId = currentUser.getUserId();
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

            if (appData.addInvestmentAccount(account)) {
                clearFields();
                Notification.showInfo("Investment account was successfully created");

                recordTransaction(userId, initialBal, startDate, "INVESTMENT");
            } else {
                Notification.showError("An error occurred, Please try again");
            }
        } catch (Exception ex) {
            LoggerService.log(ex);
        }
    }

    private void recordTransaction(int userId, double target, String date, String goal){
        new TransactionsRepository().insert(
                new Transaction(0, userId, 0, 0, "SAVINGS",
                        target, date, 9, goal));
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
        if (riskLevel.getValue() == null || riskLevel.getValue().isEmpty()){
            Notification.showError("Please select a risk level first.");
            return;
        }
        double gain = simulateAnnualReturn(
                selected.getPrincipal(), selected.getInterestRate(),
                selected.getDurationMonths(), riskLevel.getValue());
        investmentTable.refresh();
        String sign = gain >= 0 ? "gain" : "loss";
        Notification.showInfo(String.format(
                "Risk level: %s on GHS %.2f\nSimulated a %s of GHS %.2f.",
                riskLevel.getValue(), selected.getPrincipal(), sign, Math.abs(gain)));
    }

    public double simulateAnnualReturn(double principal,
                                       double annualRate,
                                       int durationMonths,
                                       String riskLevel) {

        double varianceFactor = switch (riskLevel == null ? "" : riskLevel) {
            case "Low" -> 0.02;      // ±2%
            case "Medium" -> 0.08;   // ±8%
            case "High" -> 0.18;     // ±18%
            default -> 0.05;         // ±5%
        };

        // Random fluctuation between -varianceFactor and +varianceFactor
        double fluctuation = (Math.random() * 2 - 1) * varianceFactor;

        // Adjust the annual rate
        double simulatedRate = annualRate * (1 + fluctuation);

        // Prevent negative returns
        simulatedRate = Math.max(simulatedRate, 0);

        // Convert duration to years
        double years = durationMonths / 12.0;

        // Calculate simple interest
        return principal * simulatedRate * years / 100.0;
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
                deposit(selected, amount);
            } else {
                withdraw(selected, amount);
            }

            simAmountField.clear();
            investmentTable.refresh();

            Notification.showInfo(String.format("%s of GHS %.2f successful. New balance: GHS %.2f",
                    action.equals("deposit") ? "Deposit" : "Withdrawal", amount, selected.getPrincipal()));
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }

    public void deposit(Investment acc, double amount) throws Exception {
        User currentUser = UserSession.getInstance().getCurrentUser();

        // Update the database savings balance
        int userId = currentUser.getUserId();
        int res = appData.depositInvestmentAccount(userId, acc.getInvestmentId(), amount);

        if (res == 1) {
            // record transaction
            boolean trans = new TransactionsRepository().insert(
                    new Transaction(0,
                            acc.getUserId(), acc.getInvestmentId(), 0,
                            "DEPOSIT", amount,
                            LocalDateTime.now().toString(), 1, "INVEST. DEPOSIT"));
        }
        else
            throw new Exception("Deposit failure");
    }

    public void withdraw(Investment acc, double amount) throws Exception {
        User currentUser = UserSession.getInstance().getCurrentUser();

        // Update the database investment balance
        int userId = currentUser.getUserId();
        int res = appData.withdrawInvestmentAccount(userId, acc.getInvestmentId(), amount);

        if (res == 1) {
            // record transaction
            boolean trans = new TransactionsRepository().insert(
                    new Transaction(0,
                            acc.getUserId(), acc.getInvestmentId(), 0,
                            "WITHDRAWAL", amount,
                            LocalDateTime.now().toString(), 2, "INVEST. WITHDRAWAL"));
        }
        else
            throw new Exception("Withdrawal failure");
    }
}
