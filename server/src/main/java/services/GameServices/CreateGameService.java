package services.GameServices;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MemoryGameDA;
import dataAccess.MySqlGameDataDA;
import models.GameData;
import results.CreateGameResult;
import requests.CreateGameRequest;
import services.Exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateGameService extends GameService{
    public CreateGameService() {}

    public String createChessGame() {
        ChessGame chessGame = new ChessGame();
        chessGame.getBoard().resetBoard();
        Gson gson = new Gson();
        return gson.toJson(chessGame);
    }

    public GameData createAndSetGameName(String gameID){
        GameData game = new GameData();
        String chessGameJson = createChessGame();
        game.setChessGame(chessGameJson);
        game.setGameName(gameID);
        return game;
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws UnauthorizedException {
        try (Connection conn = DatabaseManager.getConnection()){
            verifyAuthToken(conn, createGameRequest.getAuthToken());
            GameData gameData = createAndSetGameName(createGameRequest.getGameName());
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
            Integer generatedGameID = mySqlGameDataDA.createGame(conn, gameData);
            if (generatedGameID == null){ throw new UnauthorizedException("Error: Game Name Already in use"); }
            return new CreateGameResult(generatedGameID);
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
