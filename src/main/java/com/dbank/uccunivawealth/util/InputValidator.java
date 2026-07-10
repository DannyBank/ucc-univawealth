package com.dbank.uccunivawealth.util;

import java.util.regex.Pattern;

public class InputValidator {
    
    public static boolean isValidMsisdn(String phoneNumber) {

        Pattern PHONE_PATTERN =
                Pattern.compile("^(0\\d{9}|233\\d{9})$");
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }

        phoneNumber = phoneNumber.trim();
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean isValidUsername(String username) {
        Pattern USERNAME_PATTERN =
                Pattern.compile("^[A-Za-z][A-Za-z0-9_]{2,19}$");
        if (username == null || username.isBlank()) {
            return false;
        }

        username = username.trim();
        boolean match = USERNAME_PATTERN.matcher(username).matches();
        return match;
    }

    public static boolean isEmailValid(String email) {
        Pattern EMAIL_PATTERN = Pattern.compile(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$" );
        if(email == null) {
            return false;
        }

        email = email.trim();

        if(email.isEmpty()) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }
}
