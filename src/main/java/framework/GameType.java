package framework;

public enum GameType {
//    Reversi("Reversi"),
    TicTacToe("Tic-tac-toe");

    private final String token;
    GameType(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public static GameType fromString(String str) {
        for (GameType type : GameType.values()) {
            if (type.getToken().equals(str)) {
                return type;
            }
        }
        return null;
    }
}
