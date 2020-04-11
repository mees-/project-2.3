package tictactoe;

import framework.*;

public class Game implements GameInterface
{
    private static final String GAME_NAME = "Tic-Tac-Toe";
    private final Board board;
    private GameState lastTurn;
    private Boolean printToCommandLine = false;

    public Game() {
        board = new Board();
        lastTurn = null;
    }

    @Override
    public BoardInterface getBoard() {
        return board;
    }

    @Override
    public GameState doMove(Move move) throws InvalidMoveException, InvalidTurnException {

        if(!validCurrentTurn(move.getPlayer())) {
            throw new InvalidTurnException(move.getPlayer() + " took two turns in a row, only one is allowed.");
        }

        CellContent cellContent = move.getPlayer() == GameState.TurnOne ? CellContent.Local : CellContent.Remote;
        board.setCell(move.getX(), move.getY(), cellContent);

        setLastTurn(move.getPlayer());

        if(printToCommandLine) {
            board.printBoard();
        }
        return getResult(move);
    }

    @Override
    public void setup(GameState gameState) {
        board.reset();
    }

    private GameState getResult(Move move) {
        // todo CheckForWin laten controleren welke speler gewonnen heeft.
        if(board.checkForWin() && move.getPlayer() == GameState.TurnOne) {
            return GameState.OneWin;
        }
        else if(board.checkForWin() && move.getPlayer() == GameState.TurnTwo) {
            return GameState.TwoWin;
        }
        else if(board.boardIsFull()) {
            return GameState.Draw;
        }
        else if(move.getPlayer() == GameState.TurnTwo) {
            return GameState.TurnOne;
        }
        else if(move.getPlayer() == GameState.TurnOne) {
            return GameState.TurnTwo;
        }
        return null;
    }

    private boolean validCurrentTurn(GameState player) {
        if(lastTurn == null) {
            lastTurn = player;
            return true;
        }
        else {
            return lastTurn != player;
        }
    }

    private void setLastTurn(GameState player) {
        lastTurn = player;
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
