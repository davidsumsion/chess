package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    Integer gameID;
    String username;

    public JoinObserver(String authToken, Integer gameID, String username) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
