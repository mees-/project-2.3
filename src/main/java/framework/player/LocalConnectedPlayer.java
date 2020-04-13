package framework.player;

import connection.Connection;
import connection.GenericFuture;
import connection.commands.ForfeitCommand;
import connection.commands.LoginCommand;
import connection.commands.LogoutCommand;
import framework.BoardInterface;
import framework.ForfeitMove;
import framework.Move;

import java.util.Set;

public class LocalConnectedPlayer extends ComposablePlayer {
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
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        Move nextMove = super.getNextMove(board, possibleMoves, lastMove);
        if (nextMove instanceof ForfeitMove) {
            try {
                connection.executeCommand(new ForfeitCommand()).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            connection.sendMove(nextMove, gameType.getBoardSize());
        }
        return nextMove;
    }

    public GenericFuture logout() {
        return connection.executeCommand(new LogoutCommand());
    }
}
