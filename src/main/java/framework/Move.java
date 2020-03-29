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
