package tictactoe;

import framework.*;

public class Game implements GameInterface
{
    private static final String GAME_NAME = "Tic-Tac-Toe";
    private Board board;
    private MoveResult lastTurn;
    private Boolean printToCommandLine = true;

    public Game() {
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

        CellContent cellContent = move.getPlayer() == MoveResult.LocalTurn ? CellContent.Local : CellContent.Remote;
        board.setCell(move.getX(), move.getY(), cellContent);

        setLastTurn(move.getPlayer());

        if(printToCommandLine) {
            board.printBoard();
        }
        return getResult(move);
    }

    private MoveResult getResult(Move move) {
        // todo CheckForWin laten controleren welke speler gewonnen heeft.
        if(board.checkForWin() && move.getPlayer() == MoveResult.LocalTurn) {
            return MoveResult.Win;
        }
        else if(board.checkForWin() && move.getPlayer() == MoveResult.RemoteTurn) {
            return MoveResult.Loss;
        }
        else if(board.boardIsFull()) {
            return MoveResult.Draw;
        }
        else if(move.getPlayer() == MoveResult.RemoteTurn) {
            return MoveResult.LocalTurn;
        }
        else if(move.getPlayer() == MoveResult.LocalTurn) {
            return MoveResult.RemoteTurn;
        }
        return null;
    }

    private boolean validCurrentTurn(MoveResult player) {
        if(lastTurn == null) {
            lastTurn = player;
        }
        else if(lastTurn == player) {
            return false;
        }
        return true;
    }

    private void setLastTurn(MoveResult player) {
        lastTurn = player;
    }

    @Override
    public void start() {
        board.reset();
    }

    @Override
    public GameType getType() {
        return GameType.TicTacToe;
    }

    public void setPrintToCommandLine(Boolean printToCommandLine) {
        this.printToCommandLine = printToCommandLine;
    }

    public void printBoard() {
        board.printBoard();
    }
}
