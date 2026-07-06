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

    /** check if user exists in database and verify the hashed password */
    public boolean verify(String username, String password) throws SQLException {
        var userFound = new UserRepository().getUser(username, password);
        if (userFound == null || !userFound.isActive()){
            return false;
        }
        BCrypt.Result result = BCrypt.verifyer()
                .verify(password.toCharArray(), userFound.getPasswordHash());
        return result.verified;
    }
}
