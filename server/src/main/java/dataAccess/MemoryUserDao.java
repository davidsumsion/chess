package dataAccess;

import models.UserDAOModel;
import java.util.UUID;

import java.sql.Array;
import java.util.ArrayList;

public class MemoryUserDao {
    static ArrayList<UserDAOModel> userArr = new ArrayList<>();

    UserDAOModel user;
    public MemoryUserDao(UserDAOModel user){
        this.user = user;
    }

    public String getUser(){
        for (UserDAOModel dbUser : userArr){
            if (dbUser.getUsername().equals(user.getUsername())){ return user.getUsername(); }
        }
        return null;
    }
    public void createUser(){
        userArr.add(this.user);
    }
    public void createAuthToken(){
        user.setAuthToken(UUID.randomUUID().toString());
    }
}
