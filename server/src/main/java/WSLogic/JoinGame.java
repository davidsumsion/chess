package WSLogic;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MySqlGameDataDA;
import models.GameData;
import services.GameServices.JoinGameService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;

import java.sql.Connection;
import java.sql.SQLException;

public class JoinGame {
    Integer gameID;
    ChessGame.TeamColor playerColor;
    public JoinGame(Integer gameID, ChessGame.TeamColor playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public ServerMessage loadGame(){
        try {
            Connection connection = DatabaseManager.getConnection();
            JoinGameService joinGameService = new JoinGameService();
            GameData gameData = joinGameService.findGame(connection, this.gameID);
            Gson gson = new Gson();
            return new LoadGame(gson.toJson(gameData));
        } catch (DataAccessException | SQLException e) {
            return new Error("Cannot Find Game");
//            throw new RuntimeException(e);
        }
    }

    public void makeMove(ChessMove chessMove) {
        try {
            Connection connection = DatabaseManager.getConnection();
            JoinGameService joinGameService = new JoinGameService();
            GameData gameData = joinGameService.findGame(connection, this.gameID);
            String chessGameJson = gameData.getChessGame();
            Gson gson = new Gson();
            ChessGame chessGame = gson.fromJson(chessGameJson, ChessGame.class);
            chessGame.makeMove(chessMove);
            GameData updatedGameData = new GameData(gameData.getGameID(), gameData.getWhiteUsername(), gameData.getBlackUsername(), gameData.getGameName());
            updatedGameData.setChessGame(gson.toJson(chessGame));
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
            mySqlGameDataDA.updateGame(connection, updatedGameData);
        }catch (DataAccessException | SQLException e) {
//            return new Error("Cannot Find Game");
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

}
