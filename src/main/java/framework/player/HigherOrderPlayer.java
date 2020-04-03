package framework.player;

import framework.BoardInterface;
import framework.GameResult;
import framework.Move;

public class HigherOrderPlayer extends Player {
    private final Player original;

    public HigherOrderPlayer(Player original) {
        super(original.getUsername());
//        this.username = original.username;
        this.original = original;
    }

    @Override
    public Move getNextMove(BoardInterface board) {
        return original.getNextMove(board);
    }

    @Override
    public void onEnd(GameResult state) {
        original.onEnd(state);
    }


}
