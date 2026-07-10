package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.Investment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestmentsRepository {

    public List<Investment> getAll() {
        List<Investment> list = new ArrayList<>();

        String sql = "SELECT * FROM Investment";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                    list.add(new Investment(
                            rs.getInt("InvestmentId"),
                            rs.getInt("UserId"),
                            rs.getString("InvestmentName"),
                            rs.getString("InvestmentType"),
                            rs.getDouble("Principal"),
                            rs.getDouble("InterestRate"),
                            rs.getInt("DurationMonths"),
                            rs.getString("StartDate"),
                            rs.getString("MaturityDate"),
                            rs.getDouble("ExpectedReturn"),
                            rs.getString("Status")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
    }

    public Investment insert(Investment investment) throws SQLException {

        String sql = """
        INSERT INTO Investment (
            UserId,
            InvestmentName,
            InvestmentType,
            Principal,
            InterestRate,
            DurationMonths,
            StartDate,
            MaturityDate,
            ExpectedReturn,
            Status
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        RETURNING *;
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, investment.getUserId());
            ps.setString(2, investment.getInvestmentName());
            ps.setString(3, investment.getInvestmentType());
            ps.setDouble(4, investment.getPrincipal());
            ps.setDouble(5, investment.getInterestRate());
            ps.setInt(6, investment.getDurationMonths());
            ps.setString(7, investment.getStartDate());
            ps.setString(8, investment.getMaturityDate());
            ps.setDouble(9, investment.getExpectedReturn());
            ps.setString(10, investment.getStatus());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Investment saved = new Investment();

                    saved.setInvestmentId(rs.getInt("InvestmentId"));
                    saved.setUserId(rs.getInt("UserId"));
                    saved.setInvestmentName(rs.getString("InvestmentName"));
                    saved.setInvestmentType(rs.getString("InvestmentType"));
                    saved.setPrincipal(rs.getDouble("Principal"));
                    saved.setInterestRate(rs.getDouble("InterestRate"));
                    saved.setDurationMonths(rs.getInt("DurationMonths"));
                    saved.setStartDate(rs.getString("StartDate"));
                    saved.setMaturityDate(rs.getString("MaturityDate"));
                    saved.setExpectedReturn(rs.getDouble("ExpectedReturn"));
                    saved.setStatus(rs.getString("Status"));

                    return saved;
                }
            }
        }
        return null;
    }

    public void update(Investment account){ return; }

    public int update(int userId, int investmentId, double amount, int type){
        String sql = (type == 1)?
                """
                UPDATE Investment SET Principal = Principal + ? 
                                      WHERE InvestmentId = ? AND UserId = ?;
                """:
                """
                UPDATE Investment SET Principal = Principal - ? 
                                      WHERE InvestmentId = ? AND UserId = ?;
                """;
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, investmentId);
            ps.setInt(3, userId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void delete(String accountId){ return; }

    public List<Investment> getById(int userId) {
        List<Investment> list = new ArrayList<>();

        String sql = "SELECT * FROM Investment WHERE UserId = " + userId;

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Investment(
                        rs.getInt("InvestmentId"),
                        rs.getInt("UserId"),
                        rs.getString("InvestmentName"),
                        rs.getString("InvestmentType"),
                        rs.getDouble("Principal"),
                        rs.getDouble("InterestRate"),
                        rs.getInt("DurationMonths"),
                        rs.getString("StartDate"),
                        rs.getString("MaturityDate"),
                        rs.getDouble("ExpectedReturn"),
                        rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
