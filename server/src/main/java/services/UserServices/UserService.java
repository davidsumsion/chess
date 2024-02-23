package services.UserServices;

import dataAccess.MemoryUserDA;
import models.UserData;
import requests.RegisterRequest;

import java.util.UUID;

class UserService {
    public UserService(){};
    public String createAuthToken() { return UUID.randomUUID().toString(); }

    public UserData getUser(String username, String password, String email){
        UserData user = new UserData(username, password, email);
        MemoryUserDA dao = new MemoryUserDA(user);
        return dao.getUser();
    }

}
