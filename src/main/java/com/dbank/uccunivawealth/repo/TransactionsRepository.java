package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.Transaction;
import com.dbank.uccunivawealth.service.LoggerService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsRepository {

    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM Transactions";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new Transaction(
                            rs.getInt("TransactionId"),
                            rs.getInt("UserId"),
                            rs.getInt("SavingsId"),
                            rs.getInt("InvestmentId"),
                            rs.getString("TransactionType"),
                            rs.getDouble("Amount"),
                            rs.getString("TransactionDate"),
                            rs.getInt("CategoryId"),
                            rs.getString("Notes")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Transaction transaction) {
        String sql = """
        INSERT INTO Transactions (
            UserId,
            SavingsId,
            InvestmentId,
            TransactionType,
            Amount,
            TransactionDate,
            CategoryId,
            Notes
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getUserId());

            if (transaction.getSavingsId() > 0) {
                stmt.setInt(2, transaction.getSavingsId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            if (transaction.getInvestmentId() > 0) {
                stmt.setInt(3, transaction.getInvestmentId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, transaction.getType());
            stmt.setDouble(5, transaction.getAmount());
            stmt.setString(6, transaction.getTransDate());

            if (transaction.getCategory() > 0) {
                stmt.setInt(7, transaction.getCategory());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            if (transaction.getNotes() != null) {
                stmt.setString(8, transaction.getNotes());
            } else {
                stmt.setNull(8, Types.VARCHAR);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LoggerService.logError(e, "Failed to insert transaction");
            return false;
        }
    }

    public void update(Transaction account){ return; }
    public void delete(String accountId){ return; }
}
