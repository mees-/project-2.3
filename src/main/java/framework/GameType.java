package framework;

public enum GameType {
    Reversi("Reversi"),
    TicTacToe("Tic-tac-toe");

    private final String token;
    GameType(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }

    public static GameType fromString(String str) {
        for (GameType type : GameType.values()) {
            if (type.toString().equals(str)) {
                return type;
            }
        }
        return null;
    }
}
