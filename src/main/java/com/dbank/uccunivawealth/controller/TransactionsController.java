package com.dbank.uccunivawealth.controller;

import com.dbank.uccunivawealth.service.AppData;
import com.dbank.uccunivawealth.model.Transaction;
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
    @FXML
    private TableColumn<Transaction, Double> balAfterCol;
    @FXML
    private TableColumn<Transaction, Number> idCol;

    private final AppData appData = AppData.getInstance();

    @FXML
    public void initialize() {
        var transactions = appData.getAllTransactions();
        transactionsTable.setItems(transactions);

        amountCol.setCellFactory(col -> UiUtils.moneyCell());
        balAfterCol.setCellFactory(col -> UiUtils.moneyCell());

        // Sort newest first by default.
        transactions.sort(Comparator.comparingInt(Transaction::getId).reversed());
        //transactions.addListener((ListChangeListener<Transaction>) c -> transactionsTable.sort());

        idCol.setSortType(TableColumn.SortType.DESCENDING);
        transactionsTable.getSortOrder().add(idCol);
    }

    @FXML
    private void onExportSummary() {
        UiUtils.showInfo("Total transactions logged: " + appData.getAllTransactions().size());
    }
}
