package services.GameServices;

import dataAccess.MemoryGameDA;
import models.GameData;
import requests.AuthTokenRequest;
import results.ListGamesResult;
import services.Exceptions.UnauthorizedException;

public class ListGamesService extends GameService{
    public ListGamesService() {}
    public ListGamesResult listGames(AuthTokenRequest authTokenRequest) throws UnauthorizedException {
        verifyAuthToken(authTokenRequest.getAuthToken());
        String dataBaseUsername =  findUsername(authTokenRequest.getAuthToken());
        if (dataBaseUsername == null){
            throw new UnauthorizedException("Error: incorrect authtoken - no user associated");
        } else {
            MemoryGameDA game = new MemoryGameDA(new GameData());
            return new ListGamesResult(game.getListGames());
        }
    }
}
