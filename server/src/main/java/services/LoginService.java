package services;
import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemorySessionDA;
import dataAccess.MemoryUserDA;
import models.AuthData;
import models.SessionData;
import models.UserData;
import requests.LoginRequest;
import results.UserResult;

import java.util.ArrayList;

public class LoginService implements GameService{
    public LoginService(){}

    public boolean stringCompare(ArrayList<AuthData> arrList, String authToken) {
        for (AuthData authData: arrList){
            if (authData.getAuthtoken().equals(authToken)){
                return true;
            }
        }
        return false;
    }

    public UserResult login(String authToken, LoginRequest request){
        // AuthToken already added to AuthToken in Register -- Automatically logs a user in.
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        if (!stringCompare(memoryAuthTokenDA.getAuthArr(), authToken)) {
            UserResult res = new UserResult(null,null);
            res.setMessage("error: incorrect AuthToken");
            return res;
        }
        UserData user = new UserData(request.getUsername(), request.getPassword(), null);
        MemoryUserDA UserDao = new MemoryUserDA(user);
        UserData dbUser = UserDao.verifyUser();
        if (dbUser != null){
            String auth = dbUser.getAuthToken();
            MemorySessionDA SeshDAO = new MemorySessionDA(new SessionData(dbUser.getUsername(), auth));
            SeshDAO.createSesh();
            return new UserResult(dbUser.getUsername(), auth);
        } else {
            //message incorrect password
            UserResult res = new UserResult(null,null);
            res.setMessage("error: incorrect password");
            return res;
        }
    }
}
