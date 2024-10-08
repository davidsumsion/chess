package services.GameServices;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MemoryGameDA;
import dataAccess.MySqlGameDataDA;
import models.GameData;
import requests.AuthTokenRequest;
import results.ListGamesResult;
import services.Exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListGamesService extends GameService{
    public ListGamesService() {}
    public ListGamesResult listGames(AuthTokenRequest authTokenRequest) throws UnauthorizedException {
        try (Connection connection = DatabaseManager.getConnection()){
            verifyAuthToken(connection, authTokenRequest.getAuthToken());
            String dataBaseUsername =  findUsername(connection, authTokenRequest.getAuthToken());
            if (dataBaseUsername == null){
                throw new UnauthorizedException("Error: incorrect authtoken - no user associated");
            } else {
                MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
                ArrayList<GameData> myList = mySqlGameDataDA.getListGames(connection);
                return new ListGamesResult(myList);
            }
        } catch (SQLException | DataAccessException e){
            System.out.println("Unable to connect to the DB");
        }

        return null;
    }
}
