package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab dashboardTab;

    @FXML
    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == dashboardTab && dashboardController != null) {
                dashboardController.refreshDashboard();
            }
        });
    }

    /** Lets {@code MainApp} force a dashboard refresh right after seeding demo data. */
    public void refreshDashboard() {
        if (dashboardController != null) {
            dashboardController.refreshDashboard();
        }
    }
}
