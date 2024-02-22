package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import models.AuthData;
import models.GameData;
import results.CreateGameResult;
import requests.CreateGameRequest;

import java.util.UUID;

public class CreateGameService {
    public CreateGameService() {}

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        boolean indicator;
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        indicator = memoryAuthTokenDA.verifyAuthToken(createGameRequest.getAuthToken());
        if (!indicator){
            CreateGameResult result = new CreateGameResult(null);
            result.setMessage("Error: Not Authorized");
            return result;
        }

        //add variables to instance
        GameData game = new GameData();
        game.setGameName(createGameRequest.getGameName());
        MemoryGameDA dao = new MemoryGameDA(game);
        boolean bool;
        bool = dao.createGame();
        if (bool){
            return new CreateGameResult(game.getGameID());
        }
        CreateGameResult result = new CreateGameResult(null);
        result.setMessage("Error: Game Name Already in use");
        return result;
    }
}
