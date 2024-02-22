package services;

import dataAccess.MemoryGameDA;
import dataAccess.MemoryUserDA;
import models.GameData;
import models.SessionData;
import models.UserData;

public class DeleteAllService {

    public DeleteAllService() {}

    public void DeleteAll(){
        //game
        GameData game = new GameData();
        MemoryGameDA games = new MemoryGameDA(game);
        games.delete();
        //session
        SessionData sesh = new SessionData(null, null);
//        MemorySessionDA sessions = new MemorySessionDA(sesh);
//        sessions.delete();
        //user
        UserData user = new UserData(null, null, null);
        MemoryUserDA users = new MemoryUserDA(user);
        users.delete();
        //Authtoken???
    }
}
