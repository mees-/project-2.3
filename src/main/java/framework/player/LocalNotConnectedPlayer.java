package framework.player;

import connection.Connection;
import framework.BoardInterface;
import framework.Move;

import java.util.Set;

public class LocalNotConnectedPlayer extends HigherOrderPlayer {
    public LocalNotConnectedPlayer(Player original) {
        super(original);
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        Move nextMove = super.getNextMove(board, possibleMoves);
        return nextMove;
    }
}
