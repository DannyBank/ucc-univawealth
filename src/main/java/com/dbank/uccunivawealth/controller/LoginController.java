package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.LoginService;
import com.dbank.uccunivawealth.util.InputValidator;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class LoginController {

    @FXML
    MFXTextField usernameField;
    @FXML
    MFXPasswordField passwordField;

    @FXML
    Label lbl;

    @FXML
    protected void onLoginBtnClick() throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // validate user input for illegal character entries
        if (!InputValidator.isValidUsername(username) ||
            !InputValidator.isValidPassword(password)){
            return;
        }

        // validate user input for registered user
        var loginService = new LoginService();
        if (loginService.verify(username, password)){
            // user was verified successfully, proceed to dashboard
            lbl.setText("I was verified");
        } else {
            // user failed verification, ask for a retry
            lbl.setText("I failed");
            return;
        }
    }
    
    @FXML
    protected void btnCloseProgram() {
        
    }
}
