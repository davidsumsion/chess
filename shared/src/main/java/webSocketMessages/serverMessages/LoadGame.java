package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage{
    private String Game;
    public LoadGame(ServerMessageType type, String game) {
        super(type);
        Game = game;
    }
    public String getGame() {
        return Game;
    }
    public void setGame(String game) {
        Game = game;
    }
}