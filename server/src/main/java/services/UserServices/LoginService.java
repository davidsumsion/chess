package services.UserServices;

import dataAccess.MemoryAuthTokenDA;
import models.AuthData;
import models.UserData;
import requests.LoginRequest;
import results.UserResult;
import services.Exceptions.UnauthorizedException;

public class LoginService extends UserService {
    public LoginService(){}
    public UserResult login(LoginRequest request) throws UnauthorizedException {
        UserData dataBaseUser = getUser(request.getUsername(), request.getPassword(), null);
        if (dataBaseUser == null){ throw new UnauthorizedException("Error: User not found in database. Register before logging in");}
        else if (!request.getPassword().equals(dataBaseUser.getPassword())){ throw new UnauthorizedException("Error: Incorrect Password"); }
        else {
            //add AuthToken to db
            MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
            String newAuthToken = createAuthToken();
            memoryAuthTokenDA.createSession(new AuthData(request.getUsername(), newAuthToken));
            return new UserResult(request.getUsername(), newAuthToken);
        }

    }
}













