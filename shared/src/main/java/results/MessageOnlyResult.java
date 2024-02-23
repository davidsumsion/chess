package results;

public class MessageOnlyResult {
    String message;
    public MessageOnlyResult() {
    }
    public MessageOnlyResult(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
