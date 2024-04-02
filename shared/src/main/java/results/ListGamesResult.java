package results;
import models.GameData;
//

import java.util.ArrayList;

public class ListGamesResult {
    private ArrayList<GameData> games;
    private String message;

    public ListGamesResult(ArrayList<GameData> arr) {
        this.games = arr;
    }
    public ListGamesResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        StringBuilder retString = new StringBuilder();
        retString.append("ID\t\tGame Name\t\t\tWhite Username\t\tBlack Username\n");
//        int i = 0;
        for (GameData game : this.games) {
            retString.append(game.getGameID());
            retString.append("\t\t\t");
            retString.append(game.getGameName());
            retString.append("\t\t");
            retString.append(game.getWhiteUsername());
            retString.append("\t\t\t");
            retString.append(game.getBlackUsername());
            retString.append("\n");
//            i = i + 1;
        }
        return retString.toString();
    }
}
