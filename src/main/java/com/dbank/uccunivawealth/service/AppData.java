package com.dbank.uccunivawealth.service;

import com.dbank.uccunivawealth.model.Account;
import com.dbank.uccunivawealth.model.InvestmentAccount;
import com.dbank.uccunivawealth.model.SavingsAccount;
import com.dbank.uccunivawealth.model.SavingsGoal;
import com.dbank.uccunivawealth.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * In-memory data store shared by every controller in the application.
 * <p>
 * Because the UI is now split across several FXML files (dashboard, savings, investment,
 * transactions, goals), each with its own controller loaded independently by
 * {@link javafx.fxml.FXMLLoader}, the controllers need a common place to read and write
 * account data. A singleton keeps that sharing simple without introducing a dependency
 * injection framework, while still keeping the data access behind clear accessor methods
 * (encapsulation).
 */
public final class AppData {

    private static final AppData INSTANCE = new AppData();

    private final ObservableList<SavingsAccount> savingsAccounts = FXCollections.observableArrayList();
    private final ObservableList<InvestmentAccount> investmentAccounts = FXCollections.observableArrayList();
    private final ObservableList<Transaction> allTransactions = FXCollections.observableArrayList();
    private final ObservableList<SavingsGoal> goals = FXCollections.observableArrayList();

    private int nextAccountNumber = 1001;

    private AppData() {
    }

    public static AppData getInstance() {
        return INSTANCE;
    }

    public ObservableList<SavingsAccount> getSavingsAccounts() {
        return savingsAccounts;
    }

    public ObservableList<InvestmentAccount> getInvestmentAccounts() {
        return investmentAccounts;
    }

    public ObservableList<Transaction> getAllTransactions() {
        return allTransactions;
    }

    public ObservableList<SavingsGoal> getGoals() {
        return goals;
    }

    public synchronized String nextAccountId(String prefix) {
        return prefix + "-" + (nextAccountNumber++);
    }

    /** Copies every transaction currently logged on an account into the global ledger. */
    public void recordTransactionsOf(Account account) {
        allTransactions.addAll(account.getTransactions());
    }

    /** Copies only the most recent transaction logged on an account into the global ledger. */
    public void recordLatestTransactionOf(Account account) {
        var transactions = account.getTransactions();
        if (!transactions.isEmpty()) {
            allTransactions.add(transactions.get(transactions.size() - 1));
        }
    }

    /** Seeds the app with a couple of demo records so the tables aren't empty on first run. */
    public void seedDemoData() {
        SavingsAccount s1 = new SavingsAccount(nextAccountId("SAV"), "Kwame Mensah", 2500.00, 0.12);
        savingsAccounts.add(s1);
        recordTransactionsOf(s1);

        InvestmentAccount i1 = new InvestmentAccount(
                nextAccountId("INV"), "Kwame Mensah", 5000.00, "Mutual Fund", 0.15, "Medium");
        investmentAccounts.add(i1);
        recordTransactionsOf(i1);

        goals.add(new SavingsGoal("Emergency Fund", 10000, 2500, "Dec 2026"));
        goals.add(new SavingsGoal("New Laptop", 8000, 3200, "Sep 2026"));
    }
}
