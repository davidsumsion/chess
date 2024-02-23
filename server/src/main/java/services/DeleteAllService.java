package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import dataAccess.MemoryUserDA;
import models.AuthData;
import models.GameData;
import models.UserData;

public class DeleteAllService {

    public DeleteAllService() {}

    public void DeleteAll(){
        //game
        GameData game = new GameData();
        MemoryGameDA games = new MemoryGameDA(game);
        games.delete();

        //session
        AuthData auth = new AuthData(null, null);
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        memoryAuthTokenDA.delete();

        //user
        UserData user = new UserData(null, null, null);
        MemoryUserDA users = new MemoryUserDA(user);
        users.delete();
    }
}
