package dataAccess;

import models.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlGameDataDA {
    public MySqlGameDataDA() {}

    public Integer createGame(Connection connection, GameData gameData){
        //see if name is in use
        String sql = "SELECT * FROM GameDataTable WHERE gameName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, gameData.getGameName());
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    //set indicator to not null
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error accessing DB" + e.getMessage());
        }

        //add game if not in use
        String addGameSql = "INSERT INTO GameDataTable (gameName)\n" +
                "VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(addGameSql)){
            statement.setString(1, gameData.getGameName());
            if (statement.executeUpdate() == 1) {
                System.out.println("successfully inserted gameName into GameDataTable");
            } else {
                System.out.println("unsuccessful insert into GameDataTable");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //get gameID
        String getGameIDSQL = "SELECT * FROM GameDataTable WHERE gameName = ?" ;
        Integer gameInt = null;
        try (PreparedStatement stmt = connection.prepareStatement(getGameIDSQL)){
            stmt.setString(1, gameData.getGameName());
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    //set indicator to not null
                    gameInt = rs.getInt("gameID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error accessing DB" + e.getMessage());
        }
        return gameInt;
    }


}
