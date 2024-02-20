package requests;

public class JoinGameRequest {
    static String clientColor;
    static String gameID;

    public JoinGameRequest(String clientColor, String gameID) {
        this.clientColor = clientColor;
        this.gameID = gameID;
    }

    public static String getClientColor() {
        return clientColor;
    }

    public static String getGameID() {
        return gameID;
    }
}
