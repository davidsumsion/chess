package services;

import dataAccess.MemoryGameDA;
import models.GameDAOModel;
import results.CreateGameResult;
import requests.CreateGameRequest;

public class CreateGameService {
    public CreateGameService() {}

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        GameDAOModel game = new GameDAOModel();
        MemoryGameDA dao = new MemoryGameDA(game);
        String retStr = dao.createGame(createGameRequest.getGameName());
        return new CreateGameResult(retStr);
    }
}
