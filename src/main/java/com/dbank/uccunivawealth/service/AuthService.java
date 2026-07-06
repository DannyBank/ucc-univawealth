package com.dbank.uccunivawealth.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.repo.UserRepository;

import java.sql.SQLException;

public class AuthService {
    /** create a user in the database for accounts or systems use */
    public boolean register(User user){
        return new UserRepository().insert(user);
    }

    /** get a user by username and password only */
    public User getUser(String username) throws SQLException {
        return new UserRepository().getUser(username);
    }

    /** check if the given User is active */
    public boolean isUserValid(User userFound){
        return userFound != null && userFound.isActive();
    }

    /** check if user exists in database and verify the hashed password */
    public boolean verify(String hPassword, String ePassword) throws SQLException {
        BCrypt.Result result = BCrypt.verifyer().verify(ePassword.toCharArray(), hPassword);
        return result.verified;
    }

    public void sessionLogin(User user) {
        UserSession.getInstance().login(user);
    }

    public void sessionLogout(int userId) {
        UserSession.getInstance().logout();
    }
}
