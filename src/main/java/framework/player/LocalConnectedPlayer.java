package framework.player;

import connection.Connection;
import framework.BoardInterface;
import framework.GameResult;
import framework.Move;
import framework.player.Player;

public class LocalConnectedPlayer implements Player {
    private Player original;
    private Connection connection;

    public LocalConnectedPlayer(Player original, Connection connection) {
        this.original = original;
        this.connection = connection;
    }

    @Override
    public Move getNextMove(BoardInterface board) {
        Move nextMove = original.getNextMove(board);
        connection.sendMove(nextMove);
        return nextMove;
    }

    @Override
    public void onEnd(GameResult state) {
        original.onEnd(state);
    }
}
