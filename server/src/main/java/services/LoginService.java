package services;
import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryUserDA;
import models.AuthData;
import models.UserData;
import requests.LoginRequest;
import results.UserResult;

import java.util.UUID;

public class LoginService{
    public LoginService(){}
    public String createAuthToken() { return UUID.randomUUID().toString(); }

    public UserResult login(LoginRequest request) throws UnauthorizedException{
        UserData inputData = new UserData(request.getUsername(), request.getPassword(), null);
        MemoryUserDA userDao = new MemoryUserDA(inputData);
        UserData dbUser = userDao.getUser();

        if (dbUser == null){
            throw new UnauthorizedException("Error: User not found in database. Register before logging in");
        } else {
            //user is in database
            if (!request.getPassword().equals(dbUser.getPassword())){
                throw new UnauthorizedException("Error: Incorrect Password");
            } else {
                //correct password
                //create AuthToken
                String newAuthToken = createAuthToken();
                //create Session
                MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
                memoryAuthTokenDA.createSession(new AuthData(request.getUsername(), newAuthToken));
                //return result
                return new UserResult(request.getUsername(), newAuthToken);
            }
        }
    }
}













