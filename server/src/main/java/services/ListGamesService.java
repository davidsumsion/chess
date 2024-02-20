package services;

import dataAccess.MemoryGameDA;
import dataAccess.MemorySessionDA;
import dataAccess.MemoryUserDA;
import models.GameDAOModel;
import models.SessionDAOModel;
import models.UserDAOModel;
import requests.AuthTokenRequest;
import results.ListGamesResult;
import results.UserResult;

import java.util.ArrayList;

public class ListGamesService {
    public ListGamesService() {
    }

    public ListGamesResult listGames(AuthTokenRequest authTokenRequest){
        UserDAOModel user = new UserDAOModel(null, null, null);
        MemoryUserDA UserDao = new MemoryUserDA(user);
        if (authTokenRequest == null) {
            ArrayList<ArrayList<String>> emptyGames = new ArrayList<>();
            ListGamesResult res = new ListGamesResult(emptyGames);
            res.setMessage("incorrect authToken -- not authorized");
            return res;
        }
        if (UserDao.verifyAuthToken(authTokenRequest.getAuthToken())){
            MemoryGameDA game = new MemoryGameDA(new GameDAOModel());
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
