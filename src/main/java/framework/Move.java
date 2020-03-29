package framework;

public class Move {
    private final PlayerType player;
    private final int x;
    private final int y;

    public Move(PlayerType player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public PlayerType getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
