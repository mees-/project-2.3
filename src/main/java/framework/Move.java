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

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof Move) {
            Move move = (Move)obj;
            return this.player == move.player && this.x == move.x && this.y == move.y;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "x: " + getX() + " y: " + getY() + " player: " +getPlayer();
    }
}
