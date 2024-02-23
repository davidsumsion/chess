package dataAccess;

import models.AuthData;

import java.util.ArrayList;


public class MemoryAuthTokenDA {
    AuthData auth;
    static ArrayList<AuthData> authArr = new ArrayList<>();

    public MemoryAuthTokenDA() {}

    public void createSession(AuthData authData){
        //if username already in database update authToken
        authArr.add(authData);
    }

    public String getUser(String authToken){
        for (AuthData dbAuthData : authArr){
            if (dbAuthData.getAuthToken().equals(authToken)){
                return dbAuthData.getUsername();
            }
        }
        return null;
    }
    public boolean deleteSession(String authToken){
        AuthData removable = null;
        for (AuthData session: authArr){
            if (session.getAuthToken().equals(authToken)){
                session.setAuthToken("");
                session.setUsername("");
                removable = session;
            }
        }
        if (removable == null) {
            return false;
        }
        authArr.remove(removable);
        return true;
    }
    public void delete() { authArr = new ArrayList<>(); }

    public ArrayList<AuthData> getAuthArr(){ return authArr; }

    public boolean verifyAuthToken(String authToken){
        for (AuthData authData: authArr){
            if (authData.getAuthToken().equals(authToken)){
                return true;
            }
        }
        return false;
    }

}
