package dataAccess;

import models.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public GameData getGame(Connection connection, Integer gameID){
        String sql = "SELECT * FROM GameDataTable WHERE gameID=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, gameID);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()){
                    Integer id = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    //add chessGame in when you need it
                    String chessGame = rs.getString("chessGame");
                    return new GameData(id, whiteUsername,blackUsername, gameName);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<GameData> getListGames(Connection conn){
        ArrayList<GameData> games = new ArrayList<>();
        String sql = "SELECT * FROM GameDataTable";

        try (PreparedStatement statement = conn.prepareStatement(sql)){
            try (ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    Integer id = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    //add chessGame in when you need it
                    String chessGame = rs.getString("chessGame");
                    games.add(new GameData(id, whiteUsername, blackUsername, gameName));
                }

            }
        } catch (SQLException e) {
            System.out.println("error querying DB");
        }
        return games;
    }

    public void updateGame(Connection connection, GameData gameData){
        String sql = "UPDATE GameDataTable SET whiteUsername = ?, blackUsername = ?, gameName = ? WHERE gameID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(4, gameData.getGameID());
            statement.setString(1, gameData.getWhiteUsername());
            statement.setString(2, gameData.getBlackUsername());
            statement.setString(3, gameData.getGameName());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating GameDataTable");
        }
    }


}
