package myUnitTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MySqlAuthTokenDA;
import dataAccess.MySqlUserDA;
import models.AuthData;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}
