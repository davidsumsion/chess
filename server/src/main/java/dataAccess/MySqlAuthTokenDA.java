package dataAccess;


import com.google.gson.Gson;
import models.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlAuthTokenDA {

    public MySqlAuthTokenDA() throws DataAccessException {}

    public void createSession(Connection conn, AuthData authData) throws DataAccessException {
        String sql = "INSERT INTO AuthDataTable (username, authToken) VALUES (?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authData.getUsername());
            stmt.setString(2, authData.getAuthToken());
            if (stmt.executeUpdate() != 1) {
                throw new DataAccessException("incorrectly inserted");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public String getUser(Connection connection, String authToken) throws DataAccessException, SQLException {
        var statement = "SELECT * FROM AuthDataTable WHERE authToken=?";
        try (var ps = connection.prepareStatement(statement)){
            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()){
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return null;
    }

    public boolean deleteSession(Connection conn, String authToken) throws DataAccessException {
        String sql = "DELETE FROM AuthDataTable WHERE authToken = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authToken);
            if (stmt.executeUpdate() == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean verifyAuthToken(Connection connection, String authToken) throws DataAccessException {
        String sql = "SELECT * FROM AuthDataTable WHERE authToken = ?";
        String authDB = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, authToken);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    authDB = rs.getString("authToken");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return authDB != null;
    }

}
