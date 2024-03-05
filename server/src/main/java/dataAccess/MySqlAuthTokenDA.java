package dataAccess;


import com.google.gson.Gson;
import models.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlAuthTokenDA {

    MySqlAuthTokenDA() throws DataAccessException {
            configureDatabase();
    }

    public void createSession(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?,?)";
        var json = new Gson().toJson(authData);
        var id = executeUpdate(statement, authData.getUsername(), authData.getAuthToken(), json);
    }

    public String getUser(String authToken) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM AuthData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)){
                try (var rs = ps.executeQuery()){
                    if (rs.next()) {
                        return rs.getString("username");
                    }
                }
            }
        }
        return null;
    }

    public boolean deleteSession(String authToken){
//        AuthData removable = null;
//        for (AuthData session: authArr){
//            if (session.getAuthToken().equals(authToken)){
//                session.setAuthToken("");
//                session.setUsername("");
//                removable = session;
//            }
//        }
//        if (removable == null) {
//            return false;
//        }
//        authArr.remove(removable);
        return true;
    }

    public void delete() {
//        authArr = new ArrayList<>();
    }

    public boolean verifyAuthToken(String authToken){
//        for (AuthData authData: authArr){
//            if (authData.getAuthToken().equals(authToken)){
//                return true;
//            }
//        }
        return false;
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
