package services;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import models.GameData;
import requests.AuthTokenRequest;
import results.ListGamesResult;

import java.util.ArrayList;

public class ListGamesService {
    public ListGamesService() {
    }

    public ListGamesResult listGames(AuthTokenRequest authTokenRequest) throws UnauthorizedException {
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        boolean exists = memoryAuthTokenDA.verifyAuthToken(authTokenRequest.getAuthToken());
        if (!exists){ throw new UnauthorizedException("Error: Not Authorized"); }
        String dbUsername =  memoryAuthTokenDA.getUser(authTokenRequest.getAuthToken());
        if (dbUsername == null){
            throw new UnauthorizedException("Error: incorrect authtoken - no user associated");
        } else {
            MemoryGameDA game = new MemoryGameDA(new GameData());
            return new ListGamesResult(game.getListGames());
        }
    }
}
