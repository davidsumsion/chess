package services;

import dataAccess.MemoryGameDA;
import dataAccess.MemoryUserDA;
import models.GameData;
import models.UserData;
import requests.AuthTokenRequest;
import results.ListGamesResult;

import java.util.ArrayList;

public class ListGamesService {
    public ListGamesService() {
    }

    public ListGamesResult listGames(AuthTokenRequest authTokenRequest){
        UserData user = new UserData(null, null, null);
        MemoryUserDA UserDao = new MemoryUserDA(user);
        if (authTokenRequest == null) {
            ArrayList<ArrayList<String>> emptyGames = new ArrayList<>();
            ListGamesResult res = new ListGamesResult(emptyGames);
            res.setMessage("incorrect authToken -- not authorized");
            return res;
        }
        if (UserDao.verifyAuthToken(authTokenRequest.getAuthToken())){
            MemoryGameDA game = new MemoryGameDA(new GameData());
            return new ListGamesResult(game.getListGames());
        } else {
            //message incorrect password
            ArrayList<ArrayList<String>> emptyGames = new ArrayList<>();
            ListGamesResult res = new ListGamesResult(emptyGames);
            res.setMessage("incorrect authToken -- not authorized");
            return res;
        }
    }
}
