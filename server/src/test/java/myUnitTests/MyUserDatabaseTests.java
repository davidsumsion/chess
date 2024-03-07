package myUnitTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MySqlAuthTokenDA;
import models.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class MyUserDatabaseTests {
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
}
