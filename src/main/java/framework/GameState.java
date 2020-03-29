package framework;

public enum GameState {
    Win("win"),
    Loss("loss"),
    Draw("draw"),
    RemoteTurn,
    LocalTurn;

    private String token;

    GameState(String token) {
        this.token = token;
    }
    GameState() {

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
