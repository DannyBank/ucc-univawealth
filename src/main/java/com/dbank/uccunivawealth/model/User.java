package com.dbank.uccunivawealth.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private LocalDateTime dateCreated;
    private LocalDateTime lastLogin;
    private boolean isActive;
    private String accountNumber;

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public User(){}

    public User(String username, String accountNumber, String passwordHash,
                String fullName, String email, String phone,
                LocalDateTime dateCreated, LocalDateTime lastLogin,
                boolean isActive) {
        this.username = username;
        this.passwordHash = passwordHash;
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
    public LocalDateTime getDateCreated() { return dateCreated; }
    public LocalDateTime getLastLogin() { return lastLogin; }
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
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}