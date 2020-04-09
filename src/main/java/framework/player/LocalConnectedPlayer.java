package framework.player;

import connection.Connection;
import connection.commands.LoginCommand;
import framework.BoardInterface;
import framework.ForfeitMove;
import framework.Move;
import framework.player.Player;

import java.util.Set;

public class LocalConnectedPlayer extends HigherOrderPlayer {
    private final Connection connection;

    public LocalConnectedPlayer(Player original, Connection connection) {
        super(original);
        this.connection = connection;
        try {
            connection.executeCommand(new LoginCommand(getUsername())).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        Move nextMove = super.getNextMove(board, possibleMoves);
        if (nextMove instanceof ForfeitMove) {

        } else {
            connection.sendMove(nextMove);
        }
        return nextMove;
    }
}
