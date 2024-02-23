package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;
import java.util.UUID;

public class GameData {
    private Integer gameID;
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
        if (color.equals("BLACK")){
            setBlackUsername(username);
        } else if (color.equals("WHITE")){
           setWhiteUsername(username);
        }
    }
    public void resetCounter(){
        counter = 0;
    }
    @Override
    public String toString() {
        return "{gameID=" + this.gameID + ", gameName='" + this.gameName + "', whiteUsername='" + this.whiteUsername + "', blackUsername='" + this.blackUsername + "'}";
    }
}
