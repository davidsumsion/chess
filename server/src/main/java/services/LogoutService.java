package services;


import dataAccess.*;
import requests.AuthTokenRequest;
import results.UserResult;
import services.Exceptions.BadRequestException;

import java.sql.Connection;
import java.sql.SQLException;

public class LogoutService {
    public LogoutService() {}
    public UserResult logout(AuthTokenRequest request) throws BadRequestException {
        try (Connection conn = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
            boolean result = mySqlAuthTokenDA.deleteSession(conn, request.getAuthToken());

//            MemoryAuthTokenDA dao = new MemoryAuthTokenDA();
//            boolean result = dao.deleteSession(request.getAuthToken());
            if (!result){ throw new BadRequestException("Error: unable to delete"); }
            else { return new UserResult(null, null); }
        } catch (DataAccessException | SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
