package dataAccess;

import models.SessionData;
import java.util.ArrayList;

public class MemorySessionDA {
    SessionData sesh;

    static ArrayList<SessionData> seshArr = new ArrayList<>();

    public MemorySessionDA(SessionData sesh) {
        this.sesh = sesh;
    }

    public void createSesh(){
        seshArr.add(this.sesh);
    }

    public String deleteSesh(String authToken){
        for (SessionData sesh: seshArr){
            if (sesh.getAuthToken().equals(authToken)){
                sesh = null;
                return authToken;
            }
        }
        return null;
    }
    public SessionData getSesh() {
        return sesh;
    }
    public void delete(){
        seshArr = new ArrayList<>();
    }
}
