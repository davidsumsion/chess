package dataAccess;

import models.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class MySqlUserDA {
    public MySqlUserDA() {};

    public void createUser(Connection conn, UserData user, String encryptedPassword) throws DataAccessException {
        String sql = "INSERT INTO UserTable (username, hashedPassword, email, authToken) VALUES (?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getAuthToken());

            if (stmt.executeUpdate() != 1) {
                throw new DataAccessException("unsuccessful user insertion");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
