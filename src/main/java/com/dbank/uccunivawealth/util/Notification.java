package com.dbank.uccunivawealth.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Notification {

    public static void ShowAlert(Alert.AlertType alertType,
                                 String title, String header, String message){
        // Create the alert instance
        Alert alert = new Alert(alertType);

        // Set text properties
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.show();
    }

    public static void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Success");
        alert.showAndWait();
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void ShowOptions(Alert.AlertType alertType,
                                 String title, String header, String message){
        // Create the alert instance
        Alert alert = new Alert(alertType);

        // Set text properties
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
