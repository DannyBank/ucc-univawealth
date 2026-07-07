package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.SavingsAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavingsRepository {
    public List<SavingsAccount> getAll() {
        List<SavingsAccount> list = new ArrayList<>();

        String sql = "SELECT * FROM SavingsAccount";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new SavingsAccount(
                            rs.getInt("UserId"),
                            rs.getString("AccountNumber"),
                            rs.getString("AccountName"),
                            rs.getDouble("InitialBalance"),
                            rs.getDouble("InterestRate"),
                            rs.getDouble("TargetAmount"),
                            rs.getDouble("CurrentBalance"),
                            rs.getString("StartDate"),
                            rs.getString("TargetDate"),
                            rs.getString("Status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(SavingsAccount acc) {
        String sql = """
            INSERT INTO SavingsAccount (
                UserId,
                AccountNumber,
                AccountName,
                InitialBalance,
                InterestRate,
                TargetAmount,
                CurrentBalance,
                StartDate,
                TargetDate,
                Status
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DatabaseManager.connect()) {
            if (conn == null) return 0;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getAccountName());
            ps.setDouble(3, acc.getBalance());
            ps.setDouble(4, acc.getInterestRate());
            ps.setDouble(5, acc.getTargetAmount());
            ps.setDouble(6, acc.getCurrentBalance());
            ps.setString(7, acc.getStartDate());
            ps.setString(8, acc.getTargetDate());
            ps.setString(9, acc.getStatus());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public void update(SavingsAccount account){ return; }
    public void delete(String accountId){ return; }

    public SavingsAccount getByUser(int userId) {
        String sql = "SELECT * FROM SavingsAccount WHERE UserId = " + userId;
        SavingsAccount found = null;

        try (Connection conn = DatabaseManager.connect()) {
            if (conn == null) return null;

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                 while (rs.next()) {
                    found = new SavingsAccount(
                            rs.getInt("UserId"),
                            rs.getString("AccountNumber"),
                            rs.getString("AccountName"),
                            rs.getDouble("InitialBalance"),
                            rs.getDouble("InterestRate"),
                            rs.getDouble("TargetAmount"),
                            rs.getDouble("CurrentBalance"),
                            rs.getString("StartDate"),
                            rs.getString("TargetDate"),
                            rs.getString("Status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }
}