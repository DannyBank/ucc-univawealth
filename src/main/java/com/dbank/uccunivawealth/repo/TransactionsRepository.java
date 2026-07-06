package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.InvestmentAccount;
import com.dbank.uccunivawealth.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsRepository {
    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM savings_accounts";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new Transaction(
                            rs.getString("accountnumber"),
                            rs.getString("ownername"),
                            rs.getDouble("initialbalance"),
                            rs.getDouble("initialbalance")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Transaction acc) {
        String sql = "INSERT INTO savings_accounts(account_id, owner, balance, interest_rate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, acc.getAccountNumber());
                ps.setString(2, acc.getAccountNumber());
                ps.setDouble(3, acc.getAmount());
                ps.setDouble(4, acc.getBalanceAfter());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Transaction account){ return; }
    public void delete(String accountId){ return; }
}
