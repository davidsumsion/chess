package results;


import java.util.ArrayList;

public class ListGamesResult {
    private ArrayList<String> games;
    private String message;

    public ListGamesResult(ArrayList<String> Arr) {
        this.games = Arr;
    }
    public ArrayList<String> getGames() {
        return games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
