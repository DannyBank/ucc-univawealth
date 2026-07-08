package com.dbank.uccunivawealth.model;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private String dateCreated;
    private String lastLogin;
    private boolean isActive;
    private String accountNumber;

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public User(){}

    public User(int userId, String username, String accountNumber, String passwordHash,
                String fullName, String email, String phone,
                String dateCreated, String lastLogin,
                boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateCreated = dateCreated;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    //getters
    public int getUserId(){ return userId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDateCreated() { return dateCreated; }
    public String getLastLogin() { return lastLogin; }
    public boolean isActive() { return isActive; }

    //setters
    public void setUserId(int userid) { this.userId = userid; }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) { this.phone = phone; }
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}