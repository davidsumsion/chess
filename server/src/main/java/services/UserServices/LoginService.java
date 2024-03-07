package services.UserServices;

import dataAccess.*;
import models.AuthData;
import models.UserData;
import org.eclipse.jetty.server.Authentication;
import requests.LoginRequest;
import results.UserResult;
import services.Exceptions.ForbiddenException;
import services.Exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginService extends UserService {
    public LoginService(){}
    public UserResult login(LoginRequest request) throws UnauthorizedException {
        try (Connection conn = DatabaseManager.getConnection()){
            UserData dataBaseUser = getUser(conn, request.getUsername());
            if (dataBaseUser == null){ throw new UnauthorizedException("Error: User not found in database. Register before logging in");}
            else if (!verifyUser(conn, request.getUsername(), request.getPassword())){ throw new UnauthorizedException("Error: Incorrect Password"); }
            else {
                //add AuthToken to db
                MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
                String newAuthToken = createAuthToken();
                mySqlAuthTokenDA.createSession(conn, new AuthData(request.getUsername(), newAuthToken));
                return new UserResult(request.getUsername(), newAuthToken);

                //add AuthToken to db
//                MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
//                String newAuthToken = createAuthToken();
//                memoryAuthTokenDA.createSession(new AuthData(request.getUsername(), newAuthToken));
//                return new UserResult(request.getUsername(), newAuthToken);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }


    }
}













