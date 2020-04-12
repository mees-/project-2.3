package framework.player;

import framework.BoardInterface;
import framework.ForfeitMove;
import framework.Move;

import java.util.Set;

public class ForfeitablePlayer extends ComposablePlayer {
    private boolean hasForfeit = false;
    public ForfeitablePlayer(Player original) {
        super(original);
    }

    public void forfeit() {
        hasForfeit = true;
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        if (hasForfeit) {
            return new ForfeitMove(turn);
        } else {
            return super.getNextMove(board, possibleMoves, lastMove);
        }
    }
}
