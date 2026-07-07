package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.service.AuthService;
import com.dbank.uccunivawealth.util.InputValidator;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML MFXTextField usernameField;
    @FXML MFXPasswordField passwordField;
    @FXML MFXTextField emailField;
    @FXML MFXTextField msisdnField;
    @FXML Button btnLogin;
    @FXML Label lbl;

    @FXML private VBox loadingPane;
    @FXML private ProgressIndicator progressIndicator;

    @FXML
    protected void onRegisterBtnClick() {

        fade(loadingPane, true);

        Task<User> registerTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                return register();
            }
        };

        registerTask.setOnSucceeded(e -> {
            fade(loadingPane, false);

            User user = registerTask.getValue();

            if (user != null) {
                goToMainPage(user);
            }
        });

        registerTask.setOnFailed(e -> {
            fade(loadingPane, false);
            registerTask.getException().printStackTrace();
        });
        new Thread(registerTask).start();
    }

    private void goToMainPage(User user) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dbank/uccunivawealth/login-view.fxml"));

            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.refreshDashboard();

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root, 1050, 500));
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fade(Node node, boolean show) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), node);

        if (show) {
            node.setVisible(true);
            ft.setFromValue(0);
            ft.setToValue(1);
        } else {
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(e -> node.setVisible(false));
        }
        ft.play();
    }

    public User register() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String msisdn = msisdnField.getText();

        if (!InputValidator.isValidUsername(username) ||
            !InputValidator.isValidPassword(password) ||
            !InputValidator.isValidUsername(email) ||
            !InputValidator.isValidUsername(msisdn)) {
            return null;
        }

        return new AuthService().addUser(username, password, email, msisdn);
    }
}
