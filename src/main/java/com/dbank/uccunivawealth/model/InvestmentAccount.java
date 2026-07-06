package com.dbank.uccunivawealth.model;

/**
 * An investment account that can simulate an annual return based on its expected
 * return rate and risk level. Demonstrates inheritance (extends {@link Account})
 * and polymorphism (overrides {@link #getAccountCategory()} and {@link #describeYield()}).
 */
public class InvestmentAccount extends Account {

    private final String investmentType;
    /** Expected annual return expressed as a fraction, e.g. 0.15 for 15%. */
    private final double expectedReturn;
    private final String riskLevel;

    public InvestmentAccount(int userId, String accountNumber, String ownerName, double initialBalance,
                              String investmentType, double expectedReturn, String riskLevel) {
        super(userId, accountNumber, ownerName, initialBalance);
        this.investmentType = investmentType;
        this.expectedReturn = expectedReturn;
        this.riskLevel = riskLevel;
    }

    /**
     * Simulates one year of investment performance. Higher risk levels are given a wider
     * random variance around the expected return, so a "High" risk account can swing further
     * from its expected return than a "Low" risk one.
     *
     * @return the simulated gain (positive) or loss (negative) applied to the balance
     */
    public double simulateAnnualReturn() {
        double varianceFactor = switch (riskLevel == null ? "" : riskLevel) {
            case "Low" -> 0.02;
            case "Medium" -> 0.08;
            case "High" -> 0.18;
            default -> 0.05;
        };
        double randomFactor = (Math.random() * 2 - 1) * varianceFactor;
        double effectiveRate = expectedReturn + randomFactor;
        double gain = getBalance() * effectiveRate;

        adjustBalance(gain);
        logTransaction(gain >= 0 ? "RETURN GAIN" : "RETURN LOSS", gain);
        return gain;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public String getExpectedReturnDisplay() {
        return String.format("%.2f%%", expectedReturn * 100);
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    @Override
    public String getAccountCategory() {
        return "Investment";
    }

    @Override
    public String describeYield() {
        return investmentType + " investment targeting " + getExpectedReturnDisplay()
                + " p.a. (" + riskLevel + " risk).";
    }
}
