package com.dbank.uccunivawealth.model;

/**
 * Represents a personal savings target, e.g. "Buy a laptop" with a target amount,
 * amount saved so far, and a target date.
 */
public class SavingsGoal {

    private final int userId;
    private final int savingsId;
    private final String name;
    private final double targetAmount;
    private final double currentAmount;
    private final String targetDate;
    private final String status;


    public int getUserId() {
        return userId;
    }
    public int getSavingsId() {
        return savingsId;
    }

    public String getStatus() {
        return status;
    }

    public SavingsGoal(int savingsId, int userId, String name, double targetAmount,
                       double currentAmount, String targetDate, String status) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
        this.savingsId = savingsId;
        this.status = status;
        this.userId = userId;
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
