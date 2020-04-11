package framework;

public enum GameType {
    Reversi("Reversi", 8),
    TicTacToe("Tic-tac-toe", 3);

    private final String token;
    private final int boardSize;

    GameType(String token, int boardSize) {
        this.token = token;
        this.boardSize = boardSize;
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

    public int getBoardSize() {
        return boardSize;
    }
}
