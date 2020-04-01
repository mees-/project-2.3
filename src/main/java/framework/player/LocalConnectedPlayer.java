package framework.player;

import connection.Connection;
import framework.BoardInterface;
import framework.GameResult;
import framework.Move;
import framework.player.Player;

public class LocalConnectedPlayer extends HigherOrderPlayer {
    private Connection connection;

    public LocalConnectedPlayer(Player original, Connection connection) {
        super(original);
        this.connection = connection;
    }

    @Override
    public Move getNextMove(BoardInterface board) {
        Move nextMove = super.getNextMove(board);
        connection.sendMove(nextMove);
        return nextMove;
    }
}
