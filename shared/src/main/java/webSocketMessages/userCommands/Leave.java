package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    Integer gameID;

    public Leave(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
