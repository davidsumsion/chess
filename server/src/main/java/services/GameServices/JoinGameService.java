package services.GameServices;

import dataAccess.MemoryGameDA;
import models.GameData;
import requests.JoinGameRequest;
import results.MessageOnlyResult;
import services.Exceptions.BadRequestException;
import services.Exceptions.ForbiddenException;
import services.Exceptions.UnauthorizedException;

public class JoinGameService extends GameService{
    public JoinGameService(){};
    public GameData findGame(Integer gameID){
        GameData game = new GameData();
        MemoryGameDA games = new MemoryGameDA(game);
        return games.findGame(gameID);
    }
    public MessageOnlyResult joinGame(JoinGameRequest joinGameRequest) throws UnauthorizedException, ForbiddenException, BadRequestException {
        verifyAuthToken(joinGameRequest.getAuthToken());
        String dataBaseUsername = findUsername(joinGameRequest.getAuthToken());
        GameData dataBaseGame = findGame(joinGameRequest.getGameID());
        if (dataBaseGame != null){
            if (dataBaseGame.getWhiteUsername() != null && joinGameRequest.getPlayerColor().equals("WHITE") ||  dataBaseGame.getBlackUsername() != null && joinGameRequest.getPlayerColor().equals("BLACK")){
                throw new ForbiddenException("Error: Color already occupied");
            } else {
                dataBaseGame.setColor(joinGameRequest.getPlayerColor(), dataBaseUsername);
                return new MessageOnlyResult("");
            }
        }
        throw new BadRequestException("Error: Game not found in DB");
    }
}
