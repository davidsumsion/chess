package services;
import requests.RegisterRequest;
import results.UserResult;
import models.UserDAOModel;
import dataAccess.*;

public class RegisterService implements UserService {
    public RegisterService(){}
    public UserResult register(RegisterRequest request){
        UserDAOModel user = new UserDAOModel(request.getUsername(), request.getPassword(), request.getEmail());
        MemoryUserDA dao = new MemoryUserDA(user);
        String nullUser = dao.getUser();
        if (nullUser == null){
            dao.createUser();
            dao.createAuthToken();
            return new UserResult(user.getUsername(), user.getAuthToken());
        } else {
            UserResult res = new UserResult("","");
            res.setMessage("Username already in Database");
            return res;
        }
    }
}
