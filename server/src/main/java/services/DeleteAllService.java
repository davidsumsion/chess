package services;

import dataAccess.MemoryGameDA;
import dataAccess.MemorySessionDA;
import dataAccess.MemoryUserDA;
import models.GameDAOModel;
import models.SessionDAOModel;
import models.UserDAOModel;

public class DeleteAllService {

    public DeleteAllService() {}

    public void DeleteAll(){
        //game
        GameDAOModel game = new GameDAOModel();
        MemoryGameDA games = new MemoryGameDA(game);
        games.delete();
        //session
        SessionDAOModel sesh = new SessionDAOModel(null, null);
        MemorySessionDA sessions = new MemorySessionDA(sesh);
        sessions.delete();
        //user
        UserDAOModel user = new UserDAOModel(null, null, null);
        MemoryUserDA users = new MemoryUserDA(user);
        users.delete();
        //Authtoken???
    }
}
