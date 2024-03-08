package myUnitTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MySqlAuthTokenDA;
import dataAccess.MySqlGameDataDA;
import models.AuthData;
import models.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class MyAuthDatabaseTests {
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
    ///// Auth DB Tests /////
    //////////////////////////////
    @Test
    @DisplayName("Create Session Correctly")
    public void createSessionCorrectly() {
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
    public void createSessionWrong() {
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

    @Test
    @DisplayName("Verify AuthToken Wrong")
    public void verifyAuthTokenWrong() {
        try (Connection connection = DatabaseManager.getConnection()){
            MySqlAuthTokenDA mySqlAuthTokenDA = new MySqlAuthTokenDA();
            Boolean bool = mySqlAuthTokenDA.verifyAuthToken(connection, "fakeAuthToken");
            Assertions.assertEquals(false, bool);
        } catch (DataAccessException | SQLException e) {
            System.out.println("this is bad");
        }
    }


}
