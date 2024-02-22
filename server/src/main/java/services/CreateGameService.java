package services;

import dataAccess.MemoryGameDA;
import models.GameData;
import results.CreateGameResult;
import requests.CreateGameRequest;

public class CreateGameService {
    public CreateGameService() {}

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        GameData game = new GameData();
        MemoryGameDA dao = new MemoryGameDA(game);
        String retStr = dao.createGame(createGameRequest.getGameName());
        return new CreateGameResult(retStr);
    }
}
