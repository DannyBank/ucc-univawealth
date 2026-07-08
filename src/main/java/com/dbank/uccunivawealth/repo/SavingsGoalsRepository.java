package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.SavingsGoal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavingsGoalsRepository {
    public List<SavingsGoal> getAll() {
        List<SavingsGoal> list = new ArrayList<>();

        String sql = "SELECT * FROM SavingsAccount";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new SavingsGoal(
                            rs.getString("Name"),
                            rs.getDouble("TargetAmount"),
                            rs.getDouble("CurrentAmount"),
                            rs.getString("TargetDate")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(SavingsGoal acc) {
        String sql = "INSERT INTO SavingsAccount(account_id, owner, balance, interest_rate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, acc.getName());
                ps.setString(2, acc.getProgressDisplay());
                ps.setDouble(3, acc.getCurrentAmount());
                ps.setDouble(4, acc.getTargetAmount());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(SavingsGoal account){ return; }
    public void delete(String accountId){ return; }
}
