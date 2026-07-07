package com.dbank.uccunivawealth.repo;

import com.dbank.uccunivawealth.model.User;
import java.sql.*;

public class UserRepository {

    // map results from the Users table to the User class object
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("Username"),
                rs.getString("AccountNumber"),
                rs.getString("PasswordHash"),
                rs.getString("FullName"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getTimestamp("DateCreated").toLocalDateTime(),
                rs.getTimestamp("LastLogin").toLocalDateTime(),
                rs.getInt("IsActive") == 1
        );
    }

    // retrieve the details of the user to build a profile on the dashboard
    public User getUser(String username) {
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

    public User insert(User user) throws SQLException {

        String sql = """
        INSERT INTO Users (
            Username,
            AccountNumber,
            PasswordHash,
            FullName,
            Email,
            Phone,
            LastLogin,
            IsActive
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        RETURNING *
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getAccountNumber());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhone());

            if (user.getLastLogin() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(user.getLastLogin()));
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }

            ps.setInt(8, user.isActive() ? 1 : 0);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User insertedUser = new User();

                    insertedUser.setUserId(rs.getInt("UserId"));
                    insertedUser.setUsername(rs.getString("Username"));
                    insertedUser.setAccountNumber(rs.getString("AccountNumber"));
                    insertedUser.setPasswordHash(rs.getString("PasswordHash"));
                    insertedUser.setFullName(rs.getString("FullName"));
                    insertedUser.setEmail(rs.getString("Email"));
                    insertedUser.setPhone(rs.getString("Phone"));
                    insertedUser.setDateCreated(rs.getTimestamp("DateCreated").toLocalDateTime());
                    insertedUser.setLastLogin(rs.getTimestamp("LastLogin").toLocalDateTime());
                    insertedUser.setActive(rs.getInt("IsActive") == 1);

                    return insertedUser;
                }
            }
        }

        return null;
    }
}
