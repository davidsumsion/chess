package services;
import models.AuthData;
import requests.RegisterRequest;
import results.UserResult;
import models.UserData;
import dataAccess.*;

import java.util.UUID;

public class RegisterService {
    public RegisterService(){}

    public String createAuthToken() { return UUID.randomUUID().toString(); }

    public UserResult register(RegisterRequest request) throws UnauthorizedException, ForbiddenException{
        if (request.getPassword() == null){ throw new UnauthorizedException("Error: Bad Request"); }
        UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
        MemoryUserDA dao = new MemoryUserDA(user);
        UserData dbUser = dao.getUser();
        if (dbUser != null){ throw new ForbiddenException("Error: Username already in Database"); }
        else {
            String newAuthToken = createAuthToken();
            user.setAuthToken(newAuthToken);
            dao.createUser(user);
            MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
            memoryAuthTokenDA.createSession(new AuthData(user.getUsername(), user.getAuthToken()));
            return new UserResult(user.getUsername(), user.getAuthToken());
        }
    }
}
