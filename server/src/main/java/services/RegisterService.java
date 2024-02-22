package services;
import models.AuthData;
import requests.RegisterRequest;
import results.UserResult;
import models.UserData;
import dataAccess.*;

import java.util.UUID;

public class RegisterService implements UserService {
    public RegisterService(){}

    public String createAuthToken() { return UUID.randomUUID().toString(); }

    public UserResult register(RegisterRequest request){
        if (request.getPassword() == null){
            UserResult result = new UserResult(null,null);
            result.setMessage("Error: Bad Request");
            return result;
        }
        UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
        MemoryUserDA dao = new MemoryUserDA(user);
        UserData dbUser = dao.getUser();
        if (dbUser == null){
            //create authtoken
            String newAuthToken = createAuthToken();
            user.setAuthToken(newAuthToken);
            //insert user into userDB
            dao.createUser(user);
            //insert authData into AuthDB
            MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
            memoryAuthTokenDA.createSession(new AuthData(user.getUsername(), user.getAuthToken()));
            return new UserResult(user.getUsername(), user.getAuthToken());
        } else {
            UserResult result = new UserResult(null,null);
            result.setMessage("Error: Username already in Database");
            return result;
        }
    }
}
