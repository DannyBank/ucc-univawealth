package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AuthService;
import com.dbank.uccunivawealth.util.InputValidator;
import com.dbank.uccunivawealth.util.Notification;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML MFXPasswordField passwordField;
    @FXML Button btnLogin;
    @FXML Label lbl;

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
        var loginService = new AuthService();
        if (loginService.verify(username, password)){
            // user was verified successfully, proceed to dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbank/uccunivawealth/main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.refreshDashboard();

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            // user failed verification, ask for a retry
            Notification.ShowAlert(Alert.AlertType.INFORMATION,
                    "Login", "Failed", "Login Failed");
        }
    }
    
    @FXML
    protected void btnCloseProgram() {
        
    }
}
