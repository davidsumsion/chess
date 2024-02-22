package services;

import dataAccess.MemoryGameDA;
import models.GameData;
import requests.JoinGameRequest;
import results.MessageOnlyResult;

public class JoinGameService {
    public JoinGameService(){};

    public MessageOnlyResult joinGame(JoinGameRequest joinGameRequest){
//        UserDAOModel user = new UserDAOModel(null, null, null);
//        user.setAuthToken(joinGameRequest.getAuthToken);
//        MemoryUserDA users = new MemoryUserDA(user);
//        UserDAOModel dbUser = users.verifyUser();

        GameData game = new GameData();
        MemoryGameDA games = new MemoryGameDA(game);
        GameData dbGame = games.findGame(JoinGameRequest.getGameID());
        if (dbGame != null){
            //need user to set the color to the right
//            dbGame.setColor(JoinGameRequest.getClientColor(), user);
            MessageOnlyResult mess = new MessageOnlyResult();
            mess.setMessage("");
            return mess;
        }

        MessageOnlyResult mess = new MessageOnlyResult();
        mess.setMessage("");
        return mess;
    }
}
