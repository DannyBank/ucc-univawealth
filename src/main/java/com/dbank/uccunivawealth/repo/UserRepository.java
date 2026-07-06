package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.User;
import java.sql.*;

public class UserRepository {

    // map results from the Users table to the User class object
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("Username"),
                rs.getString("PasswordHash"),
                rs.getString("FullName"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getString("DateCreated"),
                rs.getString("LastLogin"),
                rs.getInt("IsActive") == 1
        );
    }

    // retrieve the details of the user to build a profile on the dashboard
    public User getUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE Username = ?";

        try (Connection conn = DatabaseManager.connect()){

            if (conn == null || conn.isClosed())
                throw new SQLException("Connection failed");

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(User user) {
        String sql = "INSERT INTO Users() VALUES ()";
        return true;
    }
}
