package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.util.Notification;
import com.dbank.uccunivawealth.util.UiUtils;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Comparator;

/**
 * Controller for {@code transactions.fxml}: a read-only, always-sorted ledger of every
 * deposit, withdrawal, interest credit and simulated return across all accounts.
 */
public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, Double> amountCol;

    private final AppData appData = AppData.getInstance();

    @FXML
    public void initialize() {
        appData.loadTransactions();
        var transactions = appData.getAllTransactions();
        transactionsTable.setItems(transactions);

        amountCol.setCellFactory(col -> UiUtils.moneyCell());

        // Sort newest first by default.
        transactions.sort(Comparator.comparingInt(Transaction::getId).reversed());
        //transactions.addListener((ListChangeListener<Transaction>) c -> transactionsTable.sort());
    }

    @FXML
    private void onExportSummary() {
        Notification.showInfo("Total transactions logged: " + appData.getAllTransactions().size());
    }
}
