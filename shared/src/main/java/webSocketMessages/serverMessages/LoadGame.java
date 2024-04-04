package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage{
    private String Game;
    public LoadGame(String game) {
        super(ServerMessageType.LOAD_GAME);
        Game = game;
    }
    public String getGame() {
        return Game;
    }
    public void setGame(String game) {
        Game = game;
    }
}
