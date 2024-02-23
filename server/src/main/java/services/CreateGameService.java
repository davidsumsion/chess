package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import models.GameData;
import results.CreateGameResult;
import requests.CreateGameRequest;

public class CreateGameService {
    public CreateGameService() {}

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws UnauthorizedException{
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        boolean exists = memoryAuthTokenDA.verifyAuthToken(createGameRequest.getAuthToken());
        if (!exists){ throw new UnauthorizedException("Error: Not Authorized"); }
        GameData game = new GameData();
        game.setGameName(createGameRequest.getGameName());
        MemoryGameDA dao = new MemoryGameDA(game);
        boolean bool = dao.createGame();
        if (!bool){ throw new UnauthorizedException("Error: Game Name Already in use"); }
        return new CreateGameResult(game.getGameID());
    }
}
