package services.UserServices;

import models.AuthData;
import requests.RegisterRequest;
import results.UserResult;
import models.UserData;
import dataAccess.*;
import services.Exceptions.ForbiddenException;
import services.Exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class RegisterService extends UserService {
    public RegisterService(){}
    public UserResult register(RegisterRequest request) throws UnauthorizedException, ForbiddenException {
        try (Connection conn = DatabaseManager.getConnection()) {
            UserData dataBaseUser = getUser(conn, request.getUsername());
            if (request.getPassword() == null){ throw new UnauthorizedException("Error: Bad Request"); }
            else if (dataBaseUser != null){ throw new ForbiddenException("Error: Username already in Database"); }
            else {
                UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
                String newAuthToken = createAuthToken();
                user.setAuthToken(newAuthToken);
                //add AuthToken to DB
                MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
                mySqlAuthTokenDA.createSession(conn, new AuthData(user.getUsername(), user.getAuthToken()));
                //createUser
                MySqlUserDA mySqlUserDA = new MySqlUserDA();
                mySqlUserDA.createUser(conn, user, encryptPassword(user.getPassword()));
                return new UserResult(user.getUsername(), user.getAuthToken());
            }
        } catch (DataAccessException | SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
