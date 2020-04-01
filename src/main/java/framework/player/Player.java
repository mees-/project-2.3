package framework.player;

import framework.BoardInterface;
import framework.GameResult;
import framework.Move;

public abstract class Player {
    private String username;

    public Player(String username) {
        if (username == null) {
            throw new RuntimeException("Username can't be null");
        }
        this.username = username;
    }

    public abstract Move getNextMove(BoardInterface board);

    public abstract void onEnd(GameResult state);

    public String getUsername() {
        return username;
    }
}
