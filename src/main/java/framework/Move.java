package framework;

public class Move {
    private final MoveResult player;
    private final int x;
    private final int y;

    public Move(MoveResult player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public MoveResult getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
