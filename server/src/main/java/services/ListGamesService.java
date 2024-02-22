package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import models.GameData;
import requests.AuthTokenRequest;
import results.ListGamesResult;

import java.util.ArrayList;

public class ListGamesService {
    public ListGamesService() {
    }

    public ListGamesResult listGames(AuthTokenRequest authTokenRequest){
        boolean indicator;
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        indicator = memoryAuthTokenDA.verifyAuthToken(authTokenRequest.getAuthToken());
        if (!indicator){
            ArrayList<GameData> emptyStringList = new ArrayList<>();
            ListGamesResult result = new ListGamesResult(emptyStringList);
            result.setMessage("Error: Not Authorized");
            return result;
        }

        String dbUsername =  memoryAuthTokenDA.getUser(authTokenRequest.getAuthToken());
        if (dbUsername == null){
            //message incorrect password
            ArrayList<GameData> emptyGames = new ArrayList<>();
            ListGamesResult res = new ListGamesResult(emptyGames);
            res.setMessage("incorrect authToken -- not user associated");
            return res;
        } else {
            MemoryGameDA game = new MemoryGameDA(new GameData());
            return new ListGamesResult(game.getListGames());
        }
    }
}
