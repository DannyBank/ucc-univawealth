package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.LoginService;
import com.dbank.uccunivawealth.util.InputValidator;
import com.dbank.uccunivawealth.util.Notification;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    MFXTextField usernameField;
    @FXML
    MFXPasswordField passwordField;

    @FXML
    Label lbl;

    @FXML
    protected void onLoginBtnClick() throws SQLException, IOException {
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/dbank/uccunivawealth/dashboard-view.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            // user failed verification, ask for a retry
            Notification.ShowAlert("Login", "Failed", "Login Failed");
        }
    }
    
    @FXML
    protected void btnCloseProgram() {
        
    }
}
