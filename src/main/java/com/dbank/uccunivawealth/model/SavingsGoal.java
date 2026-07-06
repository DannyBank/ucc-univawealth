package com.dbank.uccunivawealth.model;

/**
 * Represents a personal savings target, e.g. "Buy a laptop" with a target amount,
 * amount saved so far, and a target date.
 */
public class SavingsGoal {

    private final String name;
    private final double targetAmount;
    private double currentAmount;
    private final String targetDate;

    public SavingsGoal(String name, double targetAmount, double currentAmount, String targetDate) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
    }

    /** Fraction of the goal completed so far, clamped to the [0, 1] range for progress bars. */
    public double getProgressFraction() {
        if (targetAmount <= 0) {
            return 0;
        }
        return Math.min(1.0, currentAmount / targetAmount);
    }

    public String getProgressDisplay() {
        return String.format("%.1f%% complete", getProgressFraction() * 100);
    }

    public String getName() {
        return name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public String getTargetDate() {
        return targetDate;
    }
}
