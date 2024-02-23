package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import models.GameData;
import requests.JoinGameRequest;
import results.MessageOnlyResult;
import services.Exceptions.BadRequestException;
import services.Exceptions.ForbiddenException;
import services.Exceptions.UnauthorizedException;

public class JoinGameService {
    public JoinGameService(){};

    public MessageOnlyResult joinGame(JoinGameRequest joinGameRequest) throws UnauthorizedException, ForbiddenException, BadRequestException {
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        boolean exists = memoryAuthTokenDA.verifyAuthToken(joinGameRequest.getAuthToken());
        if (!exists){ throw new UnauthorizedException("Error: Not Authorized"); }
        String dbUsername =  memoryAuthTokenDA.getUser(joinGameRequest.getAuthToken());
        GameData game = new GameData();
        MemoryGameDA games = new MemoryGameDA(game);
        GameData dbGame = games.findGame(joinGameRequest.getGameID());
        if (dbGame != null){
            // game exists at ID
            if (dbGame.getWhiteUsername() != null && joinGameRequest.getPlayerColor().equals("WHITE") ||  dbGame.getBlackUsername() != null && joinGameRequest.getPlayerColor().equals("BLACK")){
                throw new ForbiddenException("Error: Color already occupied");
            } else {
                dbGame.setColor(joinGameRequest.getPlayerColor(), dbUsername);
                MessageOnlyResult mess = new MessageOnlyResult();
                mess.setMessage("");
                return mess;
            }
        }
        throw new BadRequestException("Error: Game not found in DB");
    }
}
