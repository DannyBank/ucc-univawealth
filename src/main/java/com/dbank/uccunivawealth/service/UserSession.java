package com.dbank.uccunivawealth.service;

import com.dbank.uccunivawealth.model.User;

public final class UserSession {

    private static final UserSession INSTANCE = new UserSession();

    private User currentUser;

    private UserSession() {}

    public static UserSession getInstance() { return INSTANCE; }

    public void login(User user) { this.currentUser = user; }

    public void logout() { this.currentUser = null; }

    public User getCurrentUser() { return currentUser; }

    public boolean isLoggedIn() { return currentUser != null; }
}