package com.dbank.uccunivawealth.model;

public class Account {
    private final int userId;
    private final String accountNumber;

    public Account(int userId, String accountNumber) {
        this.userId = userId;
        this.accountNumber = accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
