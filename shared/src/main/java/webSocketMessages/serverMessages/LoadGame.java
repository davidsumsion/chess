package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage{
    private String game;
    public LoadGame(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    public String getGame() {
        return game;
    }
    public void setGame(String game) {
        this.game = game;
    }
}
