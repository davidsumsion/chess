package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    Integer gameID;
    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
    public Integer getGameID() {
        return gameID;
    }
    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
