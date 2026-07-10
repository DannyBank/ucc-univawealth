package com.dbank.uccunivawealth.model;

import com.dbank.uccunivawealth.util.DateFormatter;

/**
 * An immutable record of a single balance-changing event on an {@link Account}.
 * Immutability here is another facet of encapsulation: once created, a transaction
 * cannot be altered, which keeps the audit trail in {@code allTransactions} trustworthy.
 */
public class Transaction {

    private final int id;
    private final int userId;
    private final int savingsId;
    private final int investmentId;
    private final String notes;

    public int getSavingsId() {
        return savingsId;
    }

    public int getInvestmentId() {
        return investmentId;
    }

    public String getTransDate() {
        return DateFormatter.formatDate(transDate);
    }

    public int getCategory() {
        return category;
    }

    private final String type;
    private final double amount;
    private final String transDate;
    private final int category;

    public Transaction(int id, int userId, int savingsId, int investmentId,
                       String type, double amount, String transDate,
                       int category, String notes) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.transDate = transDate;
        this.category = category;
        this.savingsId = savingsId;
        this.investmentId = investmentId;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }
    public int getUserId() {return userId; }
    public String getType() {
        return type;
    }
    public double getAmount() {
        return amount;
    }
    public String getNotes() {
        return notes;
    }
}
