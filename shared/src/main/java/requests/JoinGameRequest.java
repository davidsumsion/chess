package requests;

public class JoinGameRequest {
    private String playerColor;
    private Integer gameID;
    private String authToken;

    public JoinGameRequest(Integer gameID, String playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
