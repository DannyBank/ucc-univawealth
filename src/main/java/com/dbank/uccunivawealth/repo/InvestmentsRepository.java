package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.InvestmentAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestmentsRepository {
    public List<InvestmentAccount> getAll() {
        List<InvestmentAccount> list = new ArrayList<>();

        String sql = "SELECT * FROM savings_accounts";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    list.add(new InvestmentAccount(
                            rs.getInt(""),
                            rs.getString("accountnumber"),
                            rs.getString("ownername"),
                            rs.getDouble("initialbalance"),
                            rs.getString("investmenttype"),
                            rs.getDouble("initialbalance"),
                            rs.getString("investmenttype")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(InvestmentAccount acc) {
        String sql = "INSERT INTO savings_accounts(account_id, owner, balance, interest_rate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, acc.getAccountNumber());
                ps.setString(2, acc.getOwnerName());
                ps.setDouble(3, acc.getBalance());
                ps.setDouble(4, acc.getBalance());

                ps.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(InvestmentAccount account){ return; }
    public void delete(String accountId){ return; }

}
