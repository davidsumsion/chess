package services.UserServices;

import dataAccess.MemoryUserDA;
import models.UserData;
import requests.RegisterRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class UserService {
    public UserService(){};
    public String createAuthToken() { return UUID.randomUUID().toString(); }

    public UserData getUser(String username, String password, String email){
        UserData user = new UserData(username, password, email);
        MemoryUserDA dao = new MemoryUserDA(user);
        return dao.getUser();
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
