package services.UserServices;

import dataAccess.MemoryUserDA;
import models.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requests.RegisterRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class UserService {
    public UserService(){};
    public String createAuthToken() { return UUID.randomUUID().toString(); }

    public String encryptPassword(String clearPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(clearPassword);
    }

    private String readHashedPasswordFromDatabase(Connection conn, String username){
        String sql = "SELECT hashedPassword FROM UserTable WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    return rs.getString("hashedPassword");
                }
            }
            return "";
        } catch (SQLException e) {
            return "";
        }
    };

    boolean verifyUser(Connection conn, String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(conn, username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(providedClearTextPassword, hashedPassword);
    }

    public UserData getUser(Connection conn, String username) {

        String sql = "SELECT * FROM UserTable WHERE username = ?";

        UserData userData = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    String usernameDB = rs.getString("username");
                    String hashedPasswordDB = rs.getString("hashedPassword");
                    String emailDB = rs.getString("email");
//                String authToken = rs.getString("authToken");
                    userData = new UserData(usernameDB,hashedPasswordDB, emailDB);
                }
                return userData;
            }
        } catch (SQLException e){
            return userData;
        }
    }
}
