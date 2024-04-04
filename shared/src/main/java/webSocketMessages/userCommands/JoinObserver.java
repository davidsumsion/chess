package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    Integer gameID;

    public JoinObserver(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
