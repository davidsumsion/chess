package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import models.GameData;
import requests.JoinGameRequest;
import results.MessageOnlyResult;

public class JoinGameService {
    public JoinGameService(){};

    public MessageOnlyResult joinGame(JoinGameRequest joinGameRequest){
        boolean indicator;
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        indicator = memoryAuthTokenDA.verifyAuthToken(joinGameRequest.getAuthToken());
        if (!indicator){
            MessageOnlyResult result = new MessageOnlyResult();
            result.setMessage("Error: Not Authorized");
            return result;
        }

        String dbUsername =  memoryAuthTokenDA.getUser(joinGameRequest.getAuthToken());

        GameData game = new GameData();
        MemoryGameDA games = new MemoryGameDA(game);
        GameData dbGame = games.findGame(joinGameRequest.getGameID());
        if (dbGame != null){
            // game exists at ID
            //need user to set the color to the right
            dbGame.setColor(joinGameRequest.getPlayerColor(), dbUsername);
            MessageOnlyResult mess = new MessageOnlyResult();
            mess.setMessage("");
            return mess;
        }
        MessageOnlyResult mess = new MessageOnlyResult();
        mess.setMessage("Error: Game not found in DB");
        return mess;
    }
}
