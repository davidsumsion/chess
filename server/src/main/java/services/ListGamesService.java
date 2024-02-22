package services;

import dataAccess.MemoryAuthTokenDA;
import dataAccess.MemoryGameDA;
import dataAccess.MemoryUserDA;
import models.GameData;
import models.UserData;
import requests.AuthTokenRequest;
import results.ListGamesResult;
import results.MessageOnlyResult;

import java.util.ArrayList;
import java.util.List;

public class ListGamesService {
    public ListGamesService() {
    }

    public ListGamesResult listGames(AuthTokenRequest authTokenRequest){
        boolean indicator;
        MemoryAuthTokenDA memoryAuthTokenDA = new MemoryAuthTokenDA();
        indicator = memoryAuthTokenDA.verifyAuthToken(authTokenRequest.getAuthToken());
        if (!indicator){
            ArrayList<ArrayList<String>> emptyStringList = new ArrayList<ArrayList<String>>();
//            ListGamesResult result = new ListGamesResult(emptyStringList);
//            result.setMessage("Error: Not Authorized");
//            return result;
        }

        String dbUsername =  memoryAuthTokenDA.getUser(authTokenRequest.getAuthToken());
        if (dbUsername == null){
            //message incorrect password
            ArrayList<ArrayList<String>> emptyGames = new ArrayList<>();
//            ListGamesResult res = new ListGamesResult(emptyGames);
//            res.setMessage("incorrect authToken -- not user associated");
//            return res;
        } else {
            MemoryGameDA game = new MemoryGameDA(new GameData());
            return new ListGamesResult(game.getListGames());
        }
    return null;
    }
}
