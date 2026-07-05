package com.dbank.uccunivawealth.util;

import javafx.scene.control.Alert;

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
