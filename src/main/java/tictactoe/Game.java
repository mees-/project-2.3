package tictactoe;

import framework.*;

public class Game2 implements GameInterface
{
    private static final String GAME_NAME = "Tic-Tac-Toe";
    private Board board;
    private PlayerType lastTurn;

    public Game2() {
        board = new Board();
        lastTurn = null;
    }

    @Override
    public BoardInterface getBoard() {
        return board;
    }

    @Override
    public MoveResult doMove(Move move) throws InvalidMoveException {

        if(!validCurrentTurn(move.getPlayer())) {
            throw new InvalidMoveException(move.getPlayer() + " took two turns in a row, only one is allowed.");
        }

        CellContent cellContent = move.getPlayer() == PlayerType.Local ? CellContent.Local : CellContent.Remote;
        board.setCell(move.getX(), move.getY(), cellContent);

        return getResult(move);
    }

    private MoveResult getResult(Move move) {
        // todo CheckForWin laten controleren welke speler gewonnen heeft.
        if(board.checkForWin() && move.getPlayer() == PlayerType.Local) {
            return MoveResult.Win;
        }
        else if(board.checkForWin() && move.getPlayer() == PlayerType.Remote) {
            return MoveResult.Loss;
        }
        else if(board.boardIsFull()) {
            return MoveResult.Draw;
        }
        else if(move.getPlayer() == PlayerType.Local) {
            return MoveResult.RemoteMove;
        }
        else if(move.getPlayer() == PlayerType.Remote) {
            return MoveResult.LocalMove;
        }
        return null;
    }

    private boolean validCurrentTurn(PlayerType player) {
        if(lastTurn == null) {
            lastTurn = player;
        }
        else if(lastTurn == player) {
            return false;
        }
        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public GameType getType() {
        return GameType.TicTacToe;
    }
}
