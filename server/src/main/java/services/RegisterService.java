package services;
import models.AuthData;
import requests.RegisterRequest;
import results.UserResult;
import models.UserData;
import dataAccess.*;

public class RegisterService implements UserService {
    public RegisterService(){}
    public UserResult register(RegisterRequest request){
        UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
        MemoryUserDA dao = new MemoryUserDA(user);
        String nullUser = dao.getUser();
        if (nullUser == null){
            dao.createUser();
            dao.createAuthToken();
            MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
            memoryAuthTokenDA.createSession(new AuthData(user.getUsername(), user.getAuthToken()));
            return new UserResult(user.getUsername(), user.getAuthToken());
        } else {
            UserResult res = new UserResult("","");
            res.setMessage("Username already in Database");
            return res;
        }
    }
}
