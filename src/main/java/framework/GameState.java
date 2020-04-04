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
}
