package WSLogic;

/**
 * Indicates there was an error connecting to the database
 */
public class WSException extends Exception{
    public WSException(String message) {
        super(message);
    }

    public WSException(Integer in, String message) {super(message);}

}
