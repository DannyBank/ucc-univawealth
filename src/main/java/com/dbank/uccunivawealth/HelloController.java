package com.dbank.uccunivawealth;

import com.dbank.uccunivawealth.repo.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        Connection conn = DatabaseManager.connect();

        if (conn != null) {
            welcomeText.setText("Welcome to JavaFX Application!");
        } else {
            welcomeText.setText("Connection failed.");
        }
    }
}
