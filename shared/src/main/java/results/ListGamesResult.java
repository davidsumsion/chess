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
    public ArrayList<GameData> getGames() {
        return games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
