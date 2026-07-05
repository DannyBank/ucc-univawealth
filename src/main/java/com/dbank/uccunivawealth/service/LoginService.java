package com.dbank.uccunivawealth.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.dbank.uccunivawealth.repo.UserRepository;

import java.sql.SQLException;

public class LoginService {

    public boolean verify(String username, String password) throws SQLException {
        // check if user exists in database
        var userFound = new UserRepository().getUser(username, password);
        if (userFound == null || !userFound.isActive()){
            return false;
        }

        // verify the hashed password
        BCrypt.Result result = BCrypt.verifyer()
                .verify(password.toCharArray(), userFound.getPasswordHash());
        return result.verified;
    }
}
