package com.dbank.uccunivawealth.app;

import com.dbank.uccunivawealth.service.AppData;
import io.github.palexdev.materialfx.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // load user interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbank/uccunivawealth/login-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 550, 380);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/dbank/uccunivawealth/app.css")).toExternalForm());

        stage.setTitle("UnivaWealth :: Savings & Investments Manager");
        stage.setScene(scene);
        stage.show();
    }
}
