package com.dbank.uccunivawealth.model;

public class RegResponse {
    private User user;
    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
