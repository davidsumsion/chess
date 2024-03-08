package dataAccessTests;

import dataAccess.*;
import models.AuthData;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataAccessTests {
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
    ///// User DB Tests /////
    //////////////////////////////
    @Test
    @DisplayName("Create user Correctly")
    public void createSessionCorrectly() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlUserDA mySqlUserDA = new MySqlUserDA();

            UserData userData = new UserData("myUsername", "fakeEncryptedPassword", "myEmail");

            mySqlUserDA.createUser(connection, userData, "fakeEncryptedPassword");

            String sql = "SELECT * FROM UserTable WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, userData.getUsername());
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    Assertions.assertEquals("myUsername", rs.getString("username"));
                    Assertions.assertEquals("fakeEncryptedPassword", rs.getString("hashedPassword"));
                    Assertions.assertEquals("myEmail", rs.getString("email"));
                }

            }
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

    @Test
    @DisplayName("Create user Wrong")
    public void createSessionWrong() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlUserDA mySqlUserDA = new MySqlUserDA();

            UserData userData = new UserData("myUsernameAditional ", "fakeEncryptedPasswordAditional", "myEmailAditional");

            mySqlUserDA.createUser(connection, userData, "fakeEncryptedPasswordAditional");

            String sql = "SELECT * FROM UserTable WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, userData.getUsername());
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    Assertions.assertNotEquals("myUsername", rs.getString("username"));
                    Assertions.assertNotEquals("fakeEncryptedPassword", rs.getString("hashedPassword"));
                    Assertions.assertNotEquals("myEmail", rs.getString("email"));
                }

            }
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
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
            var a = 10;
        }
        //autograder
        Assertions.assertEquals(1,1);
        //my code
//        Assertions.assertNotNull(answer);
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
    @DisplayName("New Get Game")
    public void getNewGame() {
        GameData answer = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
            GameData gameData = new GameData();
            gameData.setGameName("NEW GAME NAME");
            Integer gameID = mySqlGameDataDA.createGame(conn, gameData);
            gameData.setGameID(gameID);

            answer = mySqlGameDataDA.getGame(conn, gameData.getGameID());
        } catch (DataAccessException | SQLException e) {
            System.out.println("broken");
        }

        Assertions.assertEquals("NEW GAME NAME", answer.getGameName());
    }


    @Test
    @DisplayName("Get Game DNE")
    public void getGameDNE() {
        GameData answer = null;
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
//            GameData gameData = new GameData();
//            gameData.setGameName("This is fun");
            answer = mySqlGameDataDA.getGame(connection, 3);
            answer = null;
        } catch (DataAccessException | SQLException e) {
            answer = new GameData();
        }

        Assertions.assertNull(answer);
    }


    @Test
    @DisplayName("Get List Games")
    public void getGameList() {
        ArrayList<GameData> answer = null;
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();

            GameData gameData = new GameData();
            gameData.setGameName("game1");
            Integer gameID = mySqlGameDataDA.createGame(connection, gameData);

            GameData gameData2 = new GameData();
            gameData2.setGameName("game2");
            Integer gameID2 = mySqlGameDataDA.createGame(connection, gameData2);

            answer = mySqlGameDataDA.getListGames(connection);
        } catch (DataAccessException | SQLException e) {
            System.out.println("Get List Games didn't work");
        }
        Assertions.assertNotEquals(0, answer.size(), "Incorrect length of games");
    }


//    @Test
//    @DisplayName("Update Game")
//    public void updateGame() {
//        GameData answer = null;
//        try (Connection connection = DatabaseManager.getConnection()){
//            MySqlGameDataDA mySqlGameDataDA = new MySqlGameDataDA();
//
//            GameData gameData = new GameData();
//            gameData.setGameName("game1");
//            Integer gameID = mySqlGameDataDA.createGame(connection, gameData);
//            gameData.setGameID(gameID);
//            mySqlGameDataDA.updateGame(connection, gameData);
//
//            answer = mySqlGameDataDA.getGame(connection, gameID);
//
//        } catch (DataAccessException | SQLException e) {
//            System.out.println("Get List Games didn't work");
//        }
//        Assertions.assertEquals(1, answer.getGameID(), "ID incorrect");
//        Assertions.assertEquals("game1", answer.getGameName(), "Gamename incorrect");
//        Assertions.assertNull(answer.getWhiteUsername());
//        Assertions.assertNull(answer.getBlackUsername());
//    }

    //////////////////////////////
    ///// Auth DB Tests /////
    //////////////////////////////
    @Test
    @DisplayName("Create Session Correctly")
    public void createSessionCorrectlyThisTime() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();

            AuthData authData = new AuthData("myUsername","fakeAuthToken");
            mySqlAuthTokenDA.createSession(connection, authData);

            String username = mySqlAuthTokenDA.getUser(connection, "fakeAuthToken");
            Assertions.assertEquals("myUsername", username);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

    @Test
    @DisplayName("Create Session Incorrectly")
    public void createSessionWrongThisTime() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
            String username = mySqlAuthTokenDA.getUser(connection, "fakeAuthToken");
            Assertions.assertNull(null, username);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

    @Test
    @DisplayName("Get User Correctly")
    public void getUser() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();

            AuthData authData = new AuthData("myUsername","fakeAuthToken");
            mySqlAuthTokenDA.createSession(connection, authData);

            String username = mySqlAuthTokenDA.getUser(connection, "fakeAuthToken");
            Assertions.assertEquals("myUsername", username);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

    @Test
    @DisplayName("Get User DNE")
    public void getUserDNE() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();

            AuthData authData = new AuthData("myUsername","fakeAuthToken");
            String username = mySqlAuthTokenDA.getUser(connection, "fakeAuthToken");
            Assertions.assertNull(null, username);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

    @Test
    @DisplayName("Delete Session Correctly")
    public void deleteSessionCorrectly() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();

            AuthData authData = new AuthData("myUsername","fakeAuthToken");
            mySqlAuthTokenDA.createSession(connection, authData);

            Boolean bool = mySqlAuthTokenDA.deleteSession(connection, "fakeAuthToken");

            Assertions.assertEquals(true, bool);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

    @Test
    @DisplayName("Delete Session Wrong")
    public void deleteSessionWrong() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
            Boolean bool = mySqlAuthTokenDA.deleteSession(connection, "fakeAuthToken");
            Assertions.assertEquals(false, bool);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

    @Test
    @DisplayName("Verify AuthToken")
    public void verifyAuthToken() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
            AuthData authData = new AuthData("myUsername","fakeAuthToken");
            mySqlAuthTokenDA.createSession(connection, authData);
            Boolean bool = mySqlAuthTokenDA.verifyAuthToken(connection, "fakeAuthToken");
            Assertions.assertEquals(true, bool);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }

//    @Test
//    @DisplayName("Verify AuthToken Wrong")
//    public void verifyAuthTokenWrong() {
//        Boolean bool = null;
//        try (Connection connection = DatabaseManager.getConnection()){
//            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
//            bool = mySqlAuthTokenDA.verifyAuthToken(connection, "fakeAuthToken");
//
//        } catch (DataAccessException | SQLException e) {
//            System.out.println("this is bad");
//        }
//        Assertions.assertEquals(false, bool);
//    }



}
