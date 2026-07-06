package com.dbank.uccunivawealth.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base type for every account held in the system.
 * <p>
 * This class is the anchor point for several core OOP principles required by the
 * project brief:
 * <ul>
 *     <li><b>Encapsulation</b> - the balance and transaction history are private and can only
 *     be changed through validated methods ({@link #deposit(double)}, {@link #withdraw(double)}).</li>
 *     <li><b>Abstraction</b> - {@code Account} defines the common contract shared by every account
 *     type without dictating how each subtype behaves ({@link #getAccountCategory()},
 *     {@link #describeYield()} are left to concrete subclasses).</li>
 *     <li><b>Inheritance</b> - {@link SavingsAccount} and {@link InvestmentAccount} both extend
 *     this class and reuse its balance/transaction handling.</li>
 * </ul>
 */
public abstract class Account {

    private final String accountNumber;
    private final String ownerName;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();

    protected Account(String accountNumber, String ownerName, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        if (initialBalance > 0) {
            logTransaction("OPENING BALANCE", initialBalance);
        }
    }

    /** Deposits a positive amount into the account and logs the transaction. */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        logTransaction("DEPOSIT", amount);
    }

    /** Withdraws a positive amount from the account, provided sufficient funds exist. */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException(
                    String.format("Insufficient funds. Available balance: GHS %,.2f", balance));
        }
        balance -= amount;
        logTransaction("WITHDRAWAL", -amount);
    }

    /**
     * Allows a subclass to adjust the balance for reasons other than a plain deposit/withdrawal
     * (e.g. interest credit, simulated investment return) while still keeping the field private.
     */
    protected void adjustBalance(double delta) {
        this.balance += delta;
    }

    protected void logTransaction(String type, double amount) {
        transactions.add(new Transaction(accountNumber, type, amount, balance));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /** A short label describing the kind of account, e.g. "Savings" or "Investment". */
    public abstract String getAccountCategory();

    /** A one-line human-readable description of how this account grows money. */
    public abstract String describeYield();
}
