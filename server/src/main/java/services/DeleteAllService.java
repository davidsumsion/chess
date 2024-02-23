package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import dataAccess.MemoryUserDA;
import models.AuthData;
import models.GameData;
import models.UserData;

public class DeleteAllService {

    public DeleteAllService() {}

    private void deleteGameDA(){
        GameData game = new GameData();
        MemoryGameDA games = new MemoryGameDA(game);
        games.delete();
    }
    private void deleteAuthDA(){
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        memoryAuthTokenDA.delete();
    }
    private void deleteUserDA(){
        UserData user = new UserData(null, null, null);
        MemoryUserDA users = new MemoryUserDA(user);
        users.delete();
    }
    public void DeleteAll(){
        deleteGameDA();
        deleteAuthDA();
        deleteUserDA();
    }
}
