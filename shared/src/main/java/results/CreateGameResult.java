package results;

public class CreateGameResult {
    private Integer gameID;
    private String message;

    public CreateGameResult(Integer gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
