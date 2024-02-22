package dataAccess;

import models.AuthData;

import java.sql.Array;
import java.util.ArrayList;


public class MemoryAuthTokenDA {
    AuthData auth;
    static ArrayList<AuthData> authArr = new ArrayList<>();
    public void createSession(AuthData authData){
        authArr.add(authData);
    }

    public String deleteSession(String authToken){
        for (AuthData session: authArr){
            if (session.getAuthtoken().equals(authToken)){
                session = null;
                return authToken;
            }
        }
        return null;
    }

    public void delete() { authArr = new ArrayList<>(); }

    public ArrayList<AuthData> getAuthArr(){ return authArr; }

}
