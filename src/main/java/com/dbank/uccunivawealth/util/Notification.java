package com.dbank.uccunivawealth.util;

import javafx.scene.control.Alert;

public class Notification {

    public static void ShowAlert(String title, String header, String message){
        // Create the alert instance
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Set text properties
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.show();
    }
}
