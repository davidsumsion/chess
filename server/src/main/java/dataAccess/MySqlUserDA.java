package dataAccess;

import models.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class MySqlUserDA {
    public MySqlUserDA() {};

    public void createUser(Connection conn, UserData user, String encryptedPassword) {
        String sql = "INSERT INTO UserTable (username, hashedPassword, email, authToken) VALUES (?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getAuthToken());

            if (stmt.executeUpdate() == 1) {
                System.out.println("successfully inserted");
            } else {
                System.out.println("unsuccessful insert");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }








//    public void createSession(Connection connection, AuthData authData) throws DataAccessException {
//        String sql = "insert into authDataTable (username, authToken) values (?, ?)";
//
//        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, authData.getUsername());
//            stmt.setString(2, authData.getAuthToken());
//
//            if(stmt.executeUpdate() == 1) {
//                try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                    generatedKeys.next();
//                    int id = generatedKeys.getInt(1); // ID of the inserted book
//                    authData.setId(id);
//                }
//
//                System.out.println("Inserted User into authDataTable " + authData.toString());
//            } else {
//                System.out.println("Failed to insert User into authDataTable");
//            }
//            } catch (SQLException e) {
//                throw new DataAccessException(e.getMessage());
//        }
//    }
}
