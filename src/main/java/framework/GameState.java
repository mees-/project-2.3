package framework;

public enum GameState {
    OneWin("win", true),
    TwoWin("loss", true),
    Draw("draw", true),
    TurnTwo,
    TurnOne;

    private final String token;
    private final boolean end;

    GameState(String token, boolean end) {
        this.token = token;
        this.end = end;
    }
    GameState() {
        this.token = null;
        this.end = false;
    }

    public boolean isEnd() {
        return this.end;
    }

    public static GameState fromString(String token) {
        for (GameState value : GameState.values()) {
            if (token.equalsIgnoreCase(value.token)) {
                return value;
            }
        }
        return null;
    }

    public CellContent toCellContent() {
        switch (this) {
            case TurnOne:
                return CellContent.Local;
            case TurnTwo:
                return CellContent.Remote;
            default:
                return null;
        }
    }

    public GameState otherPlayer() {
        switch (this) {
            case TurnOne:
                return TurnTwo;
            case TurnTwo:
                return TurnOne;
            case OneWin:
                return TwoWin;
            case TwoWin:
                return OneWin;
            case Draw:
                return Draw;
        }
        return this;
    }

    public GameState toWin() {
        switch (this) {
            case OneWin:
            case TwoWin:
            case Draw:
                return this;
            case TurnOne:
                return OneWin;
            case TurnTwo:
                return TwoWin;
        }
        return this;
    }

    public class InvalidOperationException extends Exception {
        public InvalidOperationException() {
        }

        public InvalidOperationException(String message) {
            super(message);
        }

        public InvalidOperationException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidOperationException(Throwable cause) {
            super(cause);
        }

        public InvalidOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
