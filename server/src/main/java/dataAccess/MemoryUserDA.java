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

    public String getUser(){
        for (UserData dbUser : userArr){
            if (dbUser.getUsername().equals(user.getUsername())){ return user.getUsername(); }
        }
        return null;
    }
    public UserData verifyUser(){
        for (UserData dbUser : userArr){
            if (dbUser.getUsername().equals(user.getUsername())) {
                if (dbUser.getPassword().equals(user.getPassword())){
                    return dbUser;
                };
                return null;
            }
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
    public void createUser(){
        userArr.add(this.user);
    }
    public void createAuthToken(){
        user.setAuthToken(UUID.randomUUID().toString());
    }

    public void delete(){
        userArr = new ArrayList<>();
    }
}
