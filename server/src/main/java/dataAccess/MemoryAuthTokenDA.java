package dataAccess;

import models.AuthData;

import java.util.ArrayList;
import java.util.UUID;


public class MemoryAuthTokenDA {
    AuthData auth;
    static ArrayList<AuthData> authArr = new ArrayList<>();

    public MemoryAuthTokenDA() {}

    public void createSession(AuthData authData){
        //if username already in database update authToken
        boolean reached = false;
        for (AuthData dbAuthData : authArr){
            if (dbAuthData.getUsername().equals(authData.getUsername())){
                dbAuthData.setAuthToken(authData.getAuthToken());
                reached = true;
                break;
            }
        }
        if (!reached) { authArr.add(authData); }
    }

    public String deleteSession(String authToken){
        for (AuthData session: authArr){
            if (session.getAuthToken().equals(authToken)){
                session = null;
                return authToken;
            }
        }
        return null;
    }
    public String createAuthToken() { return UUID.randomUUID().toString(); }

    public void delete() { authArr = new ArrayList<>(); }

    public ArrayList<AuthData> getAuthArr(){ return authArr; }

}
