package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.model.RegResponse;
import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.model.User;
import com.dbank.uccunivawealth.repo.TransactionsRepository;
import com.dbank.uccunivawealth.service.AuthService;
import com.dbank.uccunivawealth.service.LoggerService;
import com.dbank.uccunivawealth.util.InputValidator;
import com.dbank.uccunivawealth.util.Notification;
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
import java.time.LocalDate;

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

        Task<RegResponse> registerTask = new Task<>() {
            @Override
            protected RegResponse call() throws Exception {
                return register();
            }
        };

        registerTask.setOnSucceeded(e -> {
            fade(loadingPane, false);

            RegResponse response = registerTask.getValue();

            if (response != null && response.getUser() != null) {
                Notification.showInfo(response.getMessage());
                goToMainPage(response.getUser());
            } else if (response != null && !response.getMessage().isBlank()){
                Notification.showInfo(response.getMessage());
            } else {
                Notification.showInfo("An error occurred. Please try again");
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

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 550, 380));
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

    public RegResponse register() {
        RegResponse regResponse = new RegResponse();
        regResponse.setUser(null);

        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String msisdn = msisdnField.getText();

            if (!InputValidator.isValidUsername(username)){
                regResponse.setMessage("Please specify a valid username\n(No special characters)");
                return regResponse;
            }
            if (password.isBlank()){
                regResponse.setMessage("Please specify a valid password");
                return regResponse;
            }
            if (!InputValidator.isEmailValid(email)){
                regResponse.setMessage("Please specify a valid email\n(eg john@mail.com)");
                return regResponse;
            }
            if (!InputValidator.isValidMsisdn(msisdn)) {
                regResponse.setMessage("Please specify a valid msisdn\n(eg. 233xxx or 02xxx");
                return regResponse;
            }

            User user = new AuthService().addUser(username, password, email, msisdn);
            recordTransaction(user.getUserId(), 0, LocalDate.now().toString(), "REGISTER");

            regResponse.setMessage("You have successfully created an account\nPlease log in now");
            regResponse.setUser(user);

            return regResponse;

        } catch (Exception ex){
            LoggerService.logE(ex);

            regResponse.setMessage("An error occurred, please try again");

            return regResponse;
        }
    }

    public void onLoginBtnClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dbank/uccunivawealth/login-view.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root, 550, 380));
            stage.centerOnScreen();
        } catch (Exception ex){
            LoggerService.log(ex);
        }
    }

    private void recordTransaction(int userId, double target, String date, String goal){
        new TransactionsRepository().insert(
                new Transaction(0, userId, 0, 0, "REGISTER",
                        target, date, 11, goal));
    }
}
