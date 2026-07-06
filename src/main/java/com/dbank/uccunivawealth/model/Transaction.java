package com.dbank.uccunivawealth.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An immutable record of a single balance-changing event on an {@link Account}.
 * Immutability here is another facet of encapsulation: once created, a transaction
 * cannot be altered, which keeps the audit trail in {@code allTransactions} trustworthy.
 */
public class Transaction {

    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss");

    private final int id;
    private final String accountNumber;
    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;

    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this.id = SEQUENCE.getAndIncrement();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTime() {
        return timestamp.format(FORMATTER);
    }
}
