package models;

import java.util.Locale;
import java.util.UUID;

public class GameData {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;

    private static int counter = 0;

    public GameData() {
        setGameID();
    }

    public int getGameID() {
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

    public void setGameID() {
        counter += 1;
        this.gameID = counter;
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

    public void setColor(String color, String username){
        if (color == null) {return;}
        color = color.toLowerCase(Locale.ROOT);
        if (color.equals("black")){
            setBlackUsername(username);
        } else if (color.equals("white")){
           setWhiteUsername(username);
        }
    }
}
