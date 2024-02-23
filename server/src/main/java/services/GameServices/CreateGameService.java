package services.GameServices;

import dataAccess.MemoryGameDA;
import models.GameData;
import results.CreateGameResult;
import requests.CreateGameRequest;
import services.Exceptions.UnauthorizedException;

public class CreateGameService extends GameService{
    public CreateGameService() {}
    public GameData setGameName(String gameID){
        GameData game = new GameData();
        game.setGameName(gameID);
        return game;
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws UnauthorizedException {
        verifyAuthToken(createGameRequest.getAuthToken());
        GameData game = setGameName(createGameRequest.getGameName());
        MemoryGameDA dao = new MemoryGameDA(game);
        boolean bool = dao.createGame();
        if (!bool){ throw new UnauthorizedException("Error: Game Name Already in use"); }
        return new CreateGameResult(game.getGameID());
    }
}
