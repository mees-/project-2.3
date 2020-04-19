package tictactoe;

import framework.*;

public class TicTacToeGame implements GameInterface
{
    private static final String GAME_NAME = "Tic-Tac-Toe";
    private final TicTacToeBoard board;
    private GameState lastTurn;
    private Boolean printToCommandLine = false;

    public TicTacToeGame() {
        board = new TicTacToeBoard();
        lastTurn = null;
    }

    /**
     *
     * @return
     */
    @Override
    public BoardInterface getBoard() {
        return board;
    }

    /**
     *
     * @param move
     * @return
     * @throws InvalidMoveException
     * @throws InvalidTurnException
     */
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

    /**
     * Resets the board
     * @param gameState
     */
    @Override
    public void setup(GameState gameState) {
        board.reset();
    }

    /**
     *
     * @param move
     * @return
     */
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

    /**
     *
     * @param player
     * @return
     */
    private boolean validCurrentTurn(GameState player) {
        if(lastTurn == null) {
            lastTurn = player;
            return true;
        }
        else {
            return lastTurn != player;
        }
    }

    /**
     *
     * @param player
     */
    private void setLastTurn(GameState player) {
        lastTurn = player;
    }

    /**
     *
     * @return
     */
    @Override
    public GameType getType() {
        return GameType.TicTacToe;
    }

    /**
     * For demo purposes
     * Sets printing to command line
     * @param printToCommandLine
     */
    public void setPrintToCommandLine(Boolean printToCommandLine) {
        this.printToCommandLine = printToCommandLine;
    }

    /**
     * For demo purposes
     * Prints the board to the command line
     */
    public void printBoard() {
        board.printBoard();
    }
}
