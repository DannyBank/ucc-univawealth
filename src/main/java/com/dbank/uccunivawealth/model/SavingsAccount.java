package com.dbank.uccunivawealth.model;

/**
 * A savings account
 */
public class SavingsAccount extends Account{

    /** Annual interest rate expressed as a fraction, e.g. 0.12 for 12%. */
    private final double interestRate;
    private final int savingsId;
    private final double initialBalance;
    private final double targetAmount;
    private final double currentBalance;
    private final String startDate;
    private final String targetDate;
    private final String status;

    public double getInitialBalance() { return initialBalance; }
    public int getSavingsId() {
        return savingsId;
    }
    public double getTargetAmount() {
        return targetAmount;
    }
    public double getCurrentBalance() {
        return currentBalance;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getTargetDate() {
        return targetDate;
    }
    public String getStatus() {
        return status;
    }

    public SavingsAccount(int userId, String accountNumber,
                          double initialBalance, double interestRate,
                          double targetAmount, double currentBalance,
                          String startDate, String targetDate, String status) {
        super(userId, accountNumber);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        } else {
            this.interestRate = interestRate;
        }
        this.targetAmount = targetAmount;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.status = status;
        this.savingsId = 0;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getInterestRateDisplay() {
        return String.format("%.2f%%", interestRate * 100);
    }

    public void deposit(double amount) {
    }

    public void withdraw(double amount) {
    }

    public double getBalance() {
        return Double.MIN_NORMAL;
    }

    public double applyMonthlyInterest() {
        return Double.MIN_NORMAL;
    }
}
