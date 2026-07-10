package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.SavingsGoal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavingsGoalsRepository {
    public List<SavingsGoal> getAll() {
        List<SavingsGoal> list = new ArrayList<>();

        String sql = "SELECT * FROM Goals";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new SavingsGoal(
                            rs.getInt("SavingsId"),
                            rs.getInt("UserId"),
                            rs.getString("GoalName"),
                            rs.getDouble("TargetAmount"),
                            rs.getDouble("CurrentAmount"),
                            rs.getString("Deadline"),
                            rs.getString("Status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(SavingsGoal acc) {
        String sql = "INSERT INTO Goals(SavingsId,UserId,GoalName,TargetAmount,CurrentAmount,Deadline,Status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, 0);
                ps.setInt(2, acc.getUserId());
                ps.setString(3, acc.getName());
                ps.setDouble(4, acc.getTargetAmount());
                ps.setDouble(5, acc.getCurrentAmount());
                ps.setString(6, acc.getTargetDate());
                ps.setString(7, acc.getStatus());

                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void update(SavingsGoal account){ return; }
    public void delete(String accountId){ return; }

    public List<SavingsGoal> getById(int userId) {
        List<SavingsGoal> list = new ArrayList<>();

        String sql = "SELECT * FROM Goals WHERE UserId = " + userId;

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new SavingsGoal(
                            rs.getInt("SavingsId"),
                            rs.getInt("UserId"),
                            rs.getString("GoalName"),
                            rs.getDouble("TargetAmount"),
                            rs.getDouble("CurrentAmount"),
                            rs.getString("Deadline"),
                            rs.getString("Status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
