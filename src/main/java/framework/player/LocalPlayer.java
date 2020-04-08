package framework.player;

import framework.BoardInterface;
import framework.Move;

import java.util.Set;

public class LocalPlayer extends BlockingPlayer {
    public LocalPlayer(String username) {
        super(username);
    }

    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        try {
            Move sourceMove = move.take();
            for (Move possibleMove : possibleMoves) {
                if (possibleMove.getX() == sourceMove.getX() && possibleMove.getY() == sourceMove.getY()) {
                    return possibleMove;
                }
            }
            throw new RuntimeException("The move from the async source wasn't found in the possible moves");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
