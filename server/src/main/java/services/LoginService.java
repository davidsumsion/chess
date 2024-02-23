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

    public UserResult login(LoginRequest request){
        UserData inputData = new UserData(request.getUsername(), request.getPassword(), null);
        MemoryUserDA userDao = new MemoryUserDA(inputData);
        UserData dbUser = userDao.getUser();

        if (dbUser == null){
            UserResult result = new UserResult(null, null);
            result.setMessage("Error: User not found in database. Register before logging in");
            return result;
        } else {
            //user is in database
            if (!request.getPassword().equals(dbUser.getPassword())){
                //incorrect password
                UserResult result = new UserResult(null, null);
                result.setMessage("Error: Incorrect Password");
                return result;
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













