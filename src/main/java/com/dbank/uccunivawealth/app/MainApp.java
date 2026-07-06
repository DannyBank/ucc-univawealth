package com.dbank.uccunivawealth.app;

import com.dbank.uccunivawealth.controller.MainController;
import com.dbank.uccunivawealth.service.AppData;
import io.github.palexdev.materialfx.*;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dbank/uccunivawealth/login-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1050, 500);

        // Apply MaterialFX's default theme (restyles TableView, ProgressBar, ScrollPane, etc.)
        // then layer the app's own stylesheet on top for the header, summary cards and goal cards.
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/dbank/uccunivawealth/app.css")).toExternalForm());

        stage.setTitle("Savings & Investment Management System");
        stage.setScene(scene);
        stage.show();

        // Seed with a couple of demo records so the tables aren't empty on first run,
        // then force the dashboard to reflect them immediately.
        AppData.getInstance().seedDemoData();
    }
}
