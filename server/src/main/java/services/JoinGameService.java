package services;

import dataAccess.MemoryGameDA;
import dataAccess.MemoryUserDA;
import models.GameDAOModel;
import models.UserDAOModel;
import requests.JoinGameRequest;
import results.MessageOnlyResult;

public class JoinGameService {
    JoinGameService(){};

    public MessageOnlyResult joinGame(JoinGameRequest joinGameRequest){
//        UserDAOModel user = new UserDAOModel(null, null, null);
//        user.setAuthToken(joinGameRequest.getAuthToken);
//        MemoryUserDA users = new MemoryUserDA(user);
//        UserDAOModel dbUser = users.verifyUser();
        GameDAOModel game = new GameDAOModel();
        MemoryGameDA games = new MemoryGameDA(game);
        GameDAOModel dbGame = games.findGame(JoinGameRequest.getGameID());
        if (dbGame != null){
            dbGame.setColor(JoinGameRequest.getClientColor());
            MessageOnlyResult mess = new MessageOnlyResult();
            mess.setMessage("");
            return mess;
        }

    }
}
