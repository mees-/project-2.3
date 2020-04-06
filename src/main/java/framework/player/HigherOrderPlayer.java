package framework.player;

import framework.BoardInterface;
import framework.GameResult;
import framework.GameState;
import framework.Move;

import java.util.Set;

public class HigherOrderPlayer extends Player {
    private final Player original;

    protected HigherOrderPlayer(Player original) {
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

    @Override
    public GameState getTurn() {
        return original.getTurn();
    }

    @Override
    public void setTurn(GameState turn) {
        original.setTurn(turn);
    }
}
