package results;
import models.GameData;
//

import java.util.ArrayList;

public class ListGamesResult {
    private ArrayList<GameData> games;
    private String message;

    public ListGamesResult(ArrayList<GameData> Arr) {
        this.games = Arr;
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
