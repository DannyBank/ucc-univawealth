package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.service.AuthService;
import com.dbank.uccunivawealth.service.LoggerService;
import com.dbank.uccunivawealth.util.InputValidator;
import com.dbank.uccunivawealth.util.Notification;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
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
import java.sql.SQLException;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML MFXPasswordField passwordField;
    @FXML Button btnLogin;
    @FXML Label lbl;

    @FXML private VBox loadingPane;
    @FXML private ProgressIndicator progressIndicator;

    @FXML
    protected void onLoginBtnClick() {

        fade(loadingPane, true);

        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                return login();
            }
        };

        loginTask.setOnSucceeded(e -> {
            fade(loadingPane, false);

            User user = loginTask.getValue();

            if (user != null) {
                goToMainPage(user);
            } else {
                Notification.showError("Invalid username or password");
            }
        });

        loginTask.setOnFailed(e -> {
            fade(loadingPane, false);
            loginTask.getException().printStackTrace();
        });
        new Thread(loginTask).start();
    }

    private User login() throws SQLException {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (!InputValidator.isValidUsername(username) ||
                password.isBlank()) {
                return null;
            }

            AuthService authService = new AuthService();
            User user = authService.getUser(username);

            if (!authService.isUserValid(user)) {
                return null;
            }

            if (authService.verify(user.getPasswordHash(), password)) {
                authService.sessionLogin(user);
                return user;
            }
        } catch (Exception ex){
            LoggerService.log(ex);
        }
        return null;
    }

    private void goToMainPage(User user) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dbank/uccunivawealth/main-view.fxml"));

            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.refreshDashboard();

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root, 1050, 500));
            stage.centerOnScreen();

        } catch (Exception ex){
            LoggerService.log(ex);
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

    @FXML
    public void onRegisterBtnClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dbank/uccunivawealth/register.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root, 550, 550));
            stage.centerOnScreen();
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }
}
