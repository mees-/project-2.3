package framework;

public class InvalidTurnException extends Exception {
    public InvalidTurnException() {
    }

    public InvalidTurnException(String message) {
        super(message);
    }

    public InvalidTurnException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTurnException(Throwable cause) {
        super(cause);
    }

    public InvalidTurnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
