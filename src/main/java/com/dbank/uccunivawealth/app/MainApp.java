package com.dbank.uccunivawealth.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/com/dbank/uccunivawealth/dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1020, 540);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("LOGIN");
        stage.setScene(scene);
        stage.show();
    }
}
