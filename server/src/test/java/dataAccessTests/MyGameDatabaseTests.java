package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MySqlGameDataDA;
import models.GameData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyGameDatabaseTests {
    @BeforeEach
    public void cleanUp() {
        try (Connection conn = DatabaseManager.getConnection()){
            String sql = "DROP DATABASE IF EXISTS myChessDataBase;";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            DatabaseManager.createDatabase();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //////////////////////////////
    ///// GameData DB Tests /////
    //////////////////////////////
    @Test
    @DisplayName("Create Game Correctly")
    public void createGameCorectly() {
        Integer answer = null;
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
            GameData gameData = new GameData();
            gameData.setGameName("This is fun");
            answer = mySqlGameDataDA.createGame(connection, gameData);
        } catch (DataAccessException | SQLException e) {
            answer = 2;
        }
        Assertions.assertEquals(1, answer, "Did not create the correct game");
    }

    @Test
    @DisplayName("Create Game that already exists")
    public void createGameIncorrectly() {
        Integer answer = null;
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
            GameData gameData = new GameData();
            gameData.setGameName("This is fun");
            mySqlGameDataDA.createGame(connection, gameData);

            GameData newGameData = new GameData();
            newGameData.setGameName("This is fun");
            answer = mySqlGameDataDA.createGame(connection, newGameData);
        } catch (DataAccessException | SQLException e) {
            answer = 2;
        }
        Assertions.assertNull(answer, "created a game when it wasn't supposed to");
    }

    @Test
    @DisplayName("Get Game")
    public void getGame() {
        GameData answer = null;
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
            GameData gameData = new GameData();
            gameData.setGameName("This is fun");
            Integer gameID = mySqlGameDataDA.createGame(connection, gameData);
            gameData.setGameID(gameID);
            answer = mySqlGameDataDA.getGame(connection, gameData.getGameID());
        } catch (DataAccessException | SQLException e) {
            answer = null;
        }

        Assertions.assertEquals(1, answer.getGameID(), "ID incorrect");
        Assertions.assertEquals("This is fun", answer.getGameName(), "Gamename incorrect");
        Assertions.assertNull(answer.getWhiteUsername());
        Assertions.assertNull(answer.getBlackUsername());
    }


    @Test
    @DisplayName("Get Game DNE")
    public void getGameDNE() {
        GameData answer = null;
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
            GameData gameData = new GameData();
            gameData.setGameName("This is fun");
            answer = mySqlGameDataDA.getGame(connection, 1);
        } catch (DataAccessException | SQLException e) {
            answer = null;
        }

        Assertions.assertNull(answer);
    }

    @Test
    @DisplayName("Get List Games")
    public void getGameList() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();

            GameData gameData = new GameData();
            gameData.setGameName("game1");
            Integer gameID = mySqlGameDataDA.createGame(connection, gameData);

            GameData gameData2 = new GameData();
            gameData2.setGameName("game2");
            Integer gameID2 = mySqlGameDataDA.createGame(connection, gameData2);

            ArrayList<GameData> answer = mySqlGameDataDA.getListGames(connection);
            Assertions.assertEquals(2, answer.size(), "Incorrect length of games");
        } catch (DataAccessException | SQLException e) {
            System.out.println("Get List Games didn't work");
        }
    }

    @Test
    @DisplayName("Update Game")
    public void updateGame() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();

            GameData gameData = new GameData();
            gameData.setGameName("game1");
            Integer gameID = mySqlGameDataDA.createGame(connection, gameData);
            gameData.setGameID(gameID);
            mySqlGameDataDA.updateGame(connection, gameData);

            GameData answer = mySqlGameDataDA.getGame(connection, gameID);
            Assertions.assertEquals(1, answer.getGameID(), "ID incorrect");
            Assertions.assertEquals("game1", answer.getGameName(), "Gamename incorrect");
            Assertions.assertNull(answer.getWhiteUsername());
            Assertions.assertNull(answer.getBlackUsername());
        } catch (DataAccessException | SQLException e) {
            System.out.println("Get List Games didn't work");
        }
    }


}
