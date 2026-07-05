package com.dbank.uccunivawealth.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/com/dbank/uccunivawealth/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 340);

        stage.setTitle("LOGIN");
        stage.setScene(scene);
        stage.show();
    }
}
