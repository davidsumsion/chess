package results;

public class CreateGameResult {
    private String gameID;
    private String message;

    public CreateGameResult(String gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
