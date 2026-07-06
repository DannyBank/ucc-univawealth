package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.SavingsAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavingsRepository {
    public List<SavingsAccount> getAll() {
        List<SavingsAccount> list = new ArrayList<>();

        String sql = "SELECT * FROM savings_accounts";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new SavingsAccount(
                            rs.getString("account_id"),
                            rs.getString("owner"),
                            rs.getDouble("balance"),
                            rs.getDouble("interest_rate")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(SavingsAccount acc) {
        String sql = "INSERT INTO savings_accounts(account_id, owner, balance, interest_rate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, acc.getAccountNumber());
                ps.setString(2, acc.getOwnerName());
                ps.setDouble(3, acc.getBalance());
                ps.setDouble(4, acc.getInterestRate());

                ps.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
