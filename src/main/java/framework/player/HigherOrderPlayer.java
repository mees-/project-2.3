package framework.player;

import framework.BoardInterface;
import framework.GameResult;
import framework.Move;

import java.util.Set;

public class HigherOrderPlayer extends Player {
    private final Player original;

    public HigherOrderPlayer(Player original) {
        super(original.getUsername());
//        this.username = original.username;
        this.original = original;
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        return original.getNextMove(board, possibleMoves);
    }

    @Override
    public void onEnd(GameResult state) {
        original.onEnd(state);
    }


}
