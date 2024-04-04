package services.GameServices;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MemoryGameDA;
import dataAccess.MySqlGameDataDA;
import models.GameData;
import requests.JoinGameRequest;
import results.MessageOnlyResult;
import services.Exceptions.BadRequestException;
import services.Exceptions.ForbiddenException;
import services.Exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class JoinGameService extends GameService{
    public JoinGameService(){};
    public GameData findGame(Connection connection, Integer gameID) throws DataAccessException, SQLException{
        MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
        return mySqlGameDataDA.getGame(connection, gameID);
    }

    public MessageOnlyResult joinGame(JoinGameRequest joinGameRequest) throws UnauthorizedException, ForbiddenException, BadRequestException {
        try (Connection connection = DatabaseManager.getConnection()){
            verifyAuthToken(connection, joinGameRequest.getAuthToken());
            String dataBaseUsername = findUsername(connection, joinGameRequest.getAuthToken());
            GameData dataBaseGame = findGame(connection, joinGameRequest.getGameID());

            if (dataBaseGame != null){

                if (joinGameRequest.getPlayerColor() != null && dataBaseGame.getWhiteUsername() != null && joinGameRequest.getPlayerColor().equals("WHITE") ||  joinGameRequest.getPlayerColor() != null && dataBaseGame.getBlackUsername() != null && joinGameRequest.getPlayerColor().equals("BLACK")){
                    throw new ForbiddenException("Error: Color already occupied");
                } else {
                    dataBaseGame.setColor(joinGameRequest.getPlayerColor(), dataBaseUsername);
                    MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
                    mySqlGameDataDA.updateGame(connection, dataBaseGame);
                    return new MessageOnlyResult("");
                }
            }
            throw new BadRequestException("Error: Game not found in DB");
        } catch (SQLException | DataAccessException e) {
            return null;
        }
    }
}
