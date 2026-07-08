package com.dbank.uccunivawealth.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.repo.TransactionsRepository;
import com.dbank.uccunivawealth.repo.UserRepository;
import com.dbank.uccunivawealth.util.UiUtils;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthService {
    /** create a user in the database for accounts or systems use */
    public User addUser(String username, String password, String email, String msisdn) throws SQLException {
        String passwordHash = BCrypt.withDefaults()
                .hashToString(12, password.toCharArray());
        String accountNo = UiUtils.generateAccountNumber();
        String date = LocalDateTime.now().toString();

        User user = new User(0, username, accountNo, passwordHash, "n/a",
                email, msisdn, date, date, true);
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

    /** start a session with system-wide shared variables */
    public void sessionLogin(User user) {
        UserSession.getInstance().login(user);
        recordTransaction(user.getUserId(), 0, LocalDateTime.now().toString(), "LOGIN");
    }

    private void recordTransaction(int userId, double target, String date, String goal){
        new TransactionsRepository().insert(
                new Transaction(0, userId, 0, 0, "LOGIN",
                        target, date, 10, goal));
    }

    /** logging out of the system, destroy all shared variables */
    public void sessionLogout(int userId) {
        UserSession.getInstance().logout();
    }
}
