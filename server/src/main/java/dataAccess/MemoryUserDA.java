package dataAccess;

import models.UserData;
import java.util.UUID;
import java.util.ArrayList;

public class MemoryUserDA {
    UserData user;
    static ArrayList<UserData> userArr = new ArrayList<>();
    public MemoryUserDA(UserData user){
        this.user = user;
    }

    public UserData getUser(){
        for (UserData dbUser : userArr){
            if (dbUser.getUsername().equals(user.getUsername())){ return dbUser; }
        }
        return null;
    }

    public boolean verifyAuthToken(String authToken){
        for (UserData dbUser : userArr){
            if (dbUser.getAuthToken().equals(authToken)){
                return true;
            }
        }
        return false;
    }
    public void createUser(UserData userData){
        userArr.add(userData);
    }
    public void createAuthToken(){
        user.setAuthToken(UUID.randomUUID().toString());
    }

    public void delete(){
        userArr = new ArrayList<>();
    }
}
