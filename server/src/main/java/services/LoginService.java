package services;

import dataAccess.MemorySessionDA;
import dataAccess.MemoryUserDA;
import models.SessionDAOModel;
import models.UserDAOModel;
import requests.LoginRequest;
import results.UserResult;

public class LoginService implements GameService{
    public LoginService(){}

    public UserResult login(LoginRequest request){
        UserDAOModel user = new UserDAOModel(request.getUsername(), request.getPassword(), null);
        MemoryUserDA UserDao = new MemoryUserDA(user);
        UserDAOModel dbUser = UserDao.verifyUser();
        if (dbUser != null){
            String auth = dbUser.getAuthToken();
            MemorySessionDA SeshDAO = new MemorySessionDA(new SessionDAOModel(dbUser.getUsername(), auth));
            SeshDAO.createSesh();
            return new UserResult(dbUser.getUsername(), auth);
        } else {
            //message incorrect password
            UserResult res = new UserResult("","");
            res.setMessage("incorrect password");
            return res;
        }
    }
}
