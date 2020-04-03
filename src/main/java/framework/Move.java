package framework;

public class Move {
    private final GameState player;
    private final int x;
    private final int y;

    public Move(GameState player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public static Move fromSimplePosition(GameState player, int boardSize, int position) {
        return new Move(player, position % boardSize, position / boardSize);
    }

    public GameState getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
