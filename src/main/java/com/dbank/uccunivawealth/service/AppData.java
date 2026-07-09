package com.dbank.uccunivawealth.service;

import com.dbank.uccunivawealth.model.Investment;
import com.dbank.uccunivawealth.model.SavingsAccount;
import com.dbank.uccunivawealth.model.SavingsGoal;
import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.repo.InvestmentsRepository;
import com.dbank.uccunivawealth.repo.SavingsGoalsRepository;
import com.dbank.uccunivawealth.repo.SavingsRepository;
import com.dbank.uccunivawealth.repo.TransactionsRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

/**
 * In-memory data store shared by every controller in the application.
 * <p>
 * The UI is made up of several FXML files (dashboard, savings, investment,
 * transactions, goals), each with its own controller loaded independently by
 * {@link javafx.fxml.FXMLLoader}, the controllers need a common place to read and write
 * account data. A singleton keeps that sharing simple without introducing a dependency
 * injection, while still keeping the data access behind clear accessor methods
 * (encapsulation).
 */

public final class AppData {

    public AppData() {}
    private static final AppData INSTANCE = new AppData();
    public static AppData getInstance() { return INSTANCE; }

    // Repositories (database layer)
    // ============================
    private final SavingsRepository savingsRepo = new SavingsRepository();
    private final InvestmentsRepository investmentsRepo = new InvestmentsRepository();
    private final TransactionsRepository transactionsRepo = new TransactionsRepository();
    private final SavingsGoalsRepository goalsRepo = new SavingsGoalsRepository();

    // UI observable caches
    // ============================
    private final ObservableList<SavingsAccount> savingsAccounts = FXCollections.observableArrayList();
    private final ObservableList<Investment> investments = FXCollections.observableArrayList();
    private final ObservableList<Transaction> allTransactions = FXCollections.observableArrayList();
    private final ObservableList<SavingsGoal> goals = FXCollections.observableArrayList();

    // Getters for UI Binding
    // ============================
    public ObservableList<SavingsAccount> getSavingsAccounts() { return savingsAccounts; }
    public ObservableList<Investment> getInvestmentAccounts() { return investments; }
    public ObservableList<Transaction> getAllTransactions() { return allTransactions; }
    public ObservableList<SavingsGoal> getGoals() { return goals; }

    // Initial Load from Database
    // ============================
    public void loadAllData() {
        loadSavingsAccounts();
        loadInvestmentAccounts();
        loadTransactions();
        loadGoals();
    }

    public void loadSavingsAccounts() {
        savingsAccounts.setAll(savingsRepo.getAll());
    }

    public void loadSavingsAccounts(int userId) {
        savingsAccounts.setAll(savingsRepo.getByUser(userId));
    }

    public void loadInvestmentAccounts() {
        investments.setAll(investmentsRepo.getAll());
    }

    public void loadTransactions() {
        allTransactions.setAll(transactionsRepo.getAll());
    }

    public void loadGoals() {
        goals.setAll(goalsRepo.getAll());
    }

    // SAVINGS ACCOUNTS
    // ============================

    public boolean addSavingsAccount(SavingsAccount acc) {
        int insert = savingsRepo.insert(acc);// save to DB
        savingsAccounts.add(acc);            // update UI
        return insert > 0;
    }

    public void updateSavingsAccount(SavingsAccount acc) {
        savingsRepo.update(acc);
        loadSavingsAccounts(); // refresh UI
    }

    public int updateSavingsAccount(int userId, int savingsId, double amount, int type) {
        int res = savingsRepo.update(userId, savingsId, amount, type);
        if (res == 1)
            loadSavingsAccounts(); // refresh UI
        return res;
    }

    public void deleteSavingsAccount(int accountId) {
        savingsRepo.delete(String.valueOf(accountId));
        savingsAccounts.removeIf(a -> a.getSavingsId() == accountId);
    }

    // INVESTMENT ACCOUNTS
    // ============================

    public boolean addInvestmentAccount(Investment acc) throws SQLException {
        Investment insert = investmentsRepo.insert(acc);
        investments.add(acc);
        return insert != null;
    }

    public void updateInvestmentAccount(Investment acc) {
        investmentsRepo.update(acc);
        loadInvestmentAccounts();
    }

    public int updateInvestmentAccount(int userId, int investmentId, double amount, int type) {
        int res = investmentsRepo.update(userId, investmentId, amount, type);
        if (res == 1)
            loadInvestmentAccounts(); // refresh UI
        return res;
    }

    public int depositInvestmentAccount(int userId, int investmentId, double amount) {
        return updateInvestmentAccount(userId, investmentId, amount, 1);
    }

    public int withdrawInvestmentAccount(int userId, int investmentId, double amount) {
        return updateInvestmentAccount(userId, investmentId, amount, 0);
    }

    public void deleteInvestmentAccount(String accountId) {
        investmentsRepo.delete(accountId);
        investments.removeIf(a -> a.getInvestmentId().equals(accountId));
    }

    // TRANSACTIONS
    // =========================

    public void addTransaction(Transaction tx) {
        transactionsRepo.insert(tx);
        allTransactions.add(tx);
    }

    // GOALS
    // =========================

    public void addGoal(SavingsGoal goal) {
        goalsRepo.insert(goal);
        goals.add(goal);
    }

    public void updateGoal(SavingsGoal goal) {
        goalsRepo.update(goal);
        loadGoals();
    }

    public void deleteGoal(String name) {
        goalsRepo.delete(name);
        goals.removeIf(g -> g.getName().equals(name));
    }

    public int depositSavingsAccount(int userId, int savingsId, double amount) {
        return updateSavingsAccount(userId, savingsId, amount, 1);
    }

    public int withdrawSavingsAccount(int userId, int savingsId, double amount) {
        return updateSavingsAccount(userId, savingsId, amount, 0);
    }
}
