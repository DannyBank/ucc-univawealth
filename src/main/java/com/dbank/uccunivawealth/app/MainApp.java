package com.dbank.uccunivawealth.app;

import com.dbank.uccunivawealth.controller.MainController;
import com.dbank.uccunivawealth.service.AppData;
import io.github.palexdev.materialfx.*;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class MainApp extends Application {
    /*@Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/com/dbank/uccunivawealth/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 300);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("LOGIN");
        stage.setScene(scene);
        stage.show();
    }*/

    @Override
    public void start(Stage stage) throws Exception {

        // load data from the database
        AppData.getInstance().loadAllData();

        // load user interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbank/uccunivawealth/login-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1050, 500);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/dbank/uccunivawealth/app.css")).toExternalForm());

        stage.setTitle("Savings & Investment Management System");
        stage.setScene(scene);
        stage.show();
    }
}
