package dataAccess;

import models.SessionDAOModel;
import java.util.ArrayList;

public class MemorySessionDA {
    SessionDAOModel sesh;

    static ArrayList<SessionDAOModel> seshArr = new ArrayList<>();

    public MemorySessionDA(SessionDAOModel sesh) {
        this.sesh = sesh;
    }

    public void createSesh(){
        seshArr.add(this.sesh);
    }

    public String deleteSesh(String authToken){
        for (SessionDAOModel sesh: seshArr){
            if (sesh.getAuthToken().equals(authToken)){
                sesh = null;
                return authToken;
            }
        }
        return null;
    }
    public SessionDAOModel getSesh() {
        return sesh;
    }
    public void delete(){
        seshArr = new ArrayList<>();
    }
}
