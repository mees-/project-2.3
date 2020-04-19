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
     * returns the current game board.
     * @return the current game board.
     */
    @Override
    public BoardInterface getBoard() {
        return board;
    }

    /**
     *
     * @param move The next move that is taken.
     * @return The result of a successful move.
     * @throws InvalidMoveException When the move is not valid.
     * @throws InvalidTurnException When it is not the player's turn.
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
     * Resets the board for a new game.
     * @param gameState Not used.
     */
    @Override
    public void setup(GameState gameState) {
        board.reset();
    }

    /**
     * Returns the result of a successful move. This could be an end of the game,
     * (win for either players or a draw) or a next turn for either players, which
     * means the game continues.
     * @param move The next (successful) move that is taken.
     * @return either a OneWin, TwoWin, Draw, TurnOne or TurnTwo.
     */
    private GameState getResult(Move move) {
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
     * See if the current move is valid (when it is the player's turn).
     * @param player The player who made the latest (successful) move.
     * @return True when it is the player's turn, false when it
     * is not the player's turn.
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
     * Sets lastTurn to the player who made the latest successful
     * move.
     * @param player The player who made the latest move.
     */
    private void setLastTurn(GameState player) {
        lastTurn = player;
    }

    /**
     * Returns the type of game.
     * @return The gameType, in this case TicTacToe.
     */
    @Override
    public GameType getType() {
        return GameType.TicTacToe;
    }

    /**
     * For demo purposes
     * Sets printing to command line
     * @param printToCommandLine True for printing, false for no printing.
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
