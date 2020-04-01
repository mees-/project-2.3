package framework;

public enum GameType {
//    Reversi("Reversi"),
    TicTacToe("Tic-tac-toe");

    private String token;
    private GameType(String token) {
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
