package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.service.LoggerService;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:database/univawealth.db";

    public static Connection connect() throws SQLException{
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception ex){
            LoggerService.log(ex);
            return null;
        }
    }
}