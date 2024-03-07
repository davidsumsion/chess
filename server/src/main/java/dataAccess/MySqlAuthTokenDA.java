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

            if (stmt.executeUpdate() == 1) {
                System.out.println("successfully inserted");
            } else {
                System.out.println("unsuccessful insert");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public String getUser(String authToken) throws DataAccessException, SQLException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT * FROM AuthData WHERE authToken=?";
//            try (var ps = conn.prepareStatement(statement)){
//                try (var rs = ps.executeQuery()){
//                    if (rs.next()) {
//                        return rs.getString("username");
//                    }
//                }
//            }
//        }
//        return null;
//    }

    public boolean deleteSession(Connection conn, String authToken) throws DataAccessException {
        String sql = "DELETE FROM AuthDataTable WHERE authToken = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authToken);
            if (stmt.executeUpdate() == 1) {
                System.out.println("successful logged out");
                return true;
            } else {
                System.out.println("unsuccessful logged out");
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void delete() {
//        authArr = new ArrayList<>();
    }

    public boolean verifyAuthToken(Connection connection, String authToken){
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
            System.out.println(e.getMessage());
        }
        return authDB != null;
    }

    private AuthData readAuthData(ResultSet rs) throws DataAccessException, SQLException {
        var id = rs.getInt("id");
        var json = rs.getString("json");
        var authData = new Gson().fromJson(json, AuthData.class);
        authData.setId(id);
        return authData;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof AuthData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              'authToken' varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        //make conditional on database existing
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement: createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
