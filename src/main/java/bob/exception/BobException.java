package bob.exception;

/**
 * Represents exceptions for the Bob chatbot.
 */
public class BobException extends Exception {

    /**
     * Creates a new BobException instance.
     *
     * @param message the message of the thrown exception.
     */
    public BobException(String message) {
        super(message);
    }

}
