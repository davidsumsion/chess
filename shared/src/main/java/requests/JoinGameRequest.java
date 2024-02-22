package requests;

public class JoinGameRequest {
    private String clientColor;
    private Integer gameID;
    private String authToken;

    public JoinGameRequest(Integer gameID, String clientColor) {
        this.gameID = gameID;
        this.clientColor = clientColor;
    }

    public String getClientColor() {
        return clientColor;
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
