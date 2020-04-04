package framework.player;

import connection.Connection;
import framework.BoardInterface;
import framework.Move;
import framework.player.Player;

import java.util.Set;

public class LocalConnectedPlayer extends HigherOrderPlayer {
    private final Connection connection;

    public LocalConnectedPlayer(Player original, Connection connection) {
        super(original);
        this.connection = connection;
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        Move nextMove = super.getNextMove(board, possibleMoves);
        connection.sendMove(nextMove);
        return nextMove;
    }
}
