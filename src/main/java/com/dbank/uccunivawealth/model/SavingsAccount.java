package com.dbank.uccunivawealth.model;

/**
 * A savings account that earns a fixed annual interest rate, credited monthly.
 * Demonstrates inheritance (extends {@link Account}) and polymorphism
 * (overrides {@link #getAccountCategory()} and {@link #describeYield()}).
 */
public class SavingsAccount extends Account {

    /** Annual interest rate expressed as a fraction, e.g. 0.12 for 12%. */
    private final double interestRate;

    public SavingsAccount(String accountNumber, String ownerName,
                          double initialBalance, double interestRate) {
        super(accountNumber, ownerName, initialBalance);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        } else {
            this.interestRate = interestRate;
        }
    }

    /**
     * Credits one month's worth of interest to the account balance and logs it
     * as its own transaction type so it is distinguishable from a manual deposit.
     *
     * @return the amount of interest credited
     */
    public double applyMonthlyInterest() {
        double monthlyInterest = getBalance() * (interestRate / 12.0);
        if (monthlyInterest > 0) {
            adjustBalance(monthlyInterest);
            logTransaction("INTEREST", monthlyInterest);
        }
        return monthlyInterest;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getInterestRateDisplay() {
        return String.format("%.2f%%", interestRate * 100);
    }

    @Override
    public String getAccountCategory() {
        return "Savings";
    }

    @Override
    public String describeYield() {
        return "Earns " + getInterestRateDisplay() + " interest per annum, credited monthly.";
    }
}
