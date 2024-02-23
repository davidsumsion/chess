package services;
import models.AuthData;
import requests.RegisterRequest;
import results.UserResult;
import models.UserData;
import dataAccess.*;

import java.util.UUID;

public class RegisterService extends UserService {
    public RegisterService(){}

    public UserResult register(RegisterRequest request) throws UnauthorizedException, ForbiddenException{
        UserData dataBaseUser = getUser(request.getUsername(), request.getPassword(), request.getEmail());
        if (request.getPassword() == null){ throw new UnauthorizedException("Error: Bad Request"); }
        else if (dataBaseUser != null){ throw new ForbiddenException("Error: Username already in Database"); }
        else {
            UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
            String newAuthToken = createAuthToken();
            user.setAuthToken(newAuthToken);
            //add AuthToken to DB
            MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
            memoryAuthTokenDA.createSession(new AuthData(user.getUsername(), user.getAuthToken()));
            //createUser
            MemoryUserDA dao = new MemoryUserDA(user);
            dao.createUser(user);

            return new UserResult(user.getUsername(), user.getAuthToken());
        }
    }
}
