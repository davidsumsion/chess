package models;

import java.util.UUID;

public class GameDAOModel {
    private String gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;

    public GameDAOModel() {
        this.gameID = UUID.randomUUID().toString();
    }

    public String getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setColor(String color, UserDAOModel user){
       if (color.equals("black")){
            setBlackUsername(user.getUsername());
        } else if (color.equals("white")){
           setWhiteUsername(user.getUsername());
       }
    }
}
