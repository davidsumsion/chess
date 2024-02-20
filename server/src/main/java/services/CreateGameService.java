package services;

import models.GameDAOModel;
import results.CreateGameResult;
import requests.CreateGameRequest;

public class CreateGameService {
    public CreateGameService() {}

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        GameDAOModel game = new GameDAOModel();
        game.createGame(createGameRequest.getGameName());

    }
}
