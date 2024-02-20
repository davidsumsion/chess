package dataAccess;

import models.UserDAOModel;
import java.util.UUID;
import java.util.ArrayList;

public class MemoryUserDA {
    UserDAOModel user;
    static ArrayList<UserDAOModel> userArr = new ArrayList<>();
    public MemoryUserDA(UserDAOModel user){
        this.user = user;
    }

    public String getUser(){
        for (UserDAOModel dbUser : userArr){
            if (dbUser.getUsername().equals(user.getUsername())){ return user.getUsername(); }
        }
        return null;
    }
    public UserDAOModel verifyUser(){
        for (UserDAOModel dbUser : userArr){
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
        for (UserDAOModel dbUser : userArr){
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
}
