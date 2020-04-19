package tictactoe;

import framework.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicTacToeBoard extends BoardInterface {
    public static final String ANSI_RED = "\u001B[31m"; // Red color for command line
    public static final String ANSI_RESET = "\u001B[0m"; // Reset color for command line
    public static final int SIZE = 3; // Size of game board
    private final CellContent[][] board; // Two dimensional array that represents the game board

    public TicTacToeBoard() {
        board = new CellContent[SIZE][SIZE];
        reset();
    }

    /**
     * Return the content of a cell within the game board,
     * otherwise return null.
     * @param row The row of the cell.
     * @param col The column of the cell.
     * @return The cell content of a cell that is within the game
     * board (Empty, Player1 (Local) or Player2 (Remote)).
     */
    @Override
    public CellContent getCell(int row, int col) {
        if((row >= 0 && row < SIZE) && (col >= 0 && col < SIZE)) {
            return board[row][col];
        }
        return null;
    }

    /**
     * Returns the size (Length of rows & columns) of the board
     * of Tic-Tac-Toe: 3.
     * @return the size of the board.
     */
    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     * Returns all valid moves fot the current player.
     * @param state The current state of the game (which player's turn).
     * @return A set of all moves that are valid for the current player.
     */
    @Override
    public Set<Move> getValidMoves(GameState state) {
        HashSet<Move> result = new HashSet<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (getCell(row, col) == CellContent.Empty) {
                    result.add(new Move(state, row, col));
                }
            }
        }
        return result;
    }

    /**
     * Sets all board cells to empty, so the board
     * is reset to use for a new game.
     */
    @Override
    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = CellContent.Empty;
            }
        }
    }

    /**
     * Sets a cell to the current player if it is a
     * valid move.
     * @param row The row of the new move.
     * @param col The column of the new move.
     * @param cellContent The player who makes the move.
     * @throws InvalidMoveException When the move is not valid.
     */
    @Override
    public void setCell(int row, int col, CellContent cellContent) throws InvalidMoveException {
        if((row >= 0 && row < SIZE) && (col >= 0 && col < SIZE) && (board[row][col] == CellContent.Empty) && cellContent != CellContent.Empty) {
            board[row][col] = cellContent;
        }
        else {
            throw new InvalidMoveException("The move to set yPos: " + (col + 1) + " and xPos: " + (row + 1) + " to " + cellContent + " is invalid.");
        }
    }

    /**
     * Checks whether the board is full.
     * @return True if the board is full, false when it
     * is not.
     */
    public boolean boardIsFull() {
        boolean isFull = true;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == CellContent.Empty) {
                    isFull = false;
                    break;
                }
            }
        }
        return isFull;
    }

    /**
     * Initial method to check if there is a row, column or diagonal win.
     * @return true if there is a row, column or diagonal win,
     * false if there is not.
     */
    public boolean checkForWin() {
        return checkRowForWin() || checkColForWin() || checkDiaForWin();
    }

    /**
     * New method to check if there is a row, column or diagonal win.
     * @return the player who made a winning move.
     */
    public CellContent checkForWinBetter() {
        CellContent result = null;
        result = checkXForWin();
        if (result != null) {
            return result;
        }
        result = checkYForWin();
        if (result != null) {
            return result;
        }
        result = checkDiagonalForWin();
        return result;
    }

    /**
     * New method to return the player who has a
     * X win.
     * @return The player who has a Y win. otherwise
     * return null.
     */
    private CellContent checkXForWin() {
        for(int col = 0; col < SIZE; col++) {
            if(checkMarks(board[col][0], board[col][1], board[col][2])) {
                return getCell(col, 0);
            }
        }
        return null;
    }

    /**
     * Initial method to check for column win.
     * @return True if there is a column win,
     * false if not.
     */
    private boolean checkColForWin() {
        for(int col = 0; col < SIZE; col++) {
            if(checkMarks(board[col][0], board[col][1], board[col][2])) {
                return true;
            }
        }
        return false;
    }

    /**
     * New method to return the player who has a
     * Y win.
     * @return The player who has a Y win. otherwise
     * return null.
     */
    private CellContent checkYForWin() {
        for(int row = 0; row < SIZE; row++) {
            if(checkMarks(board[0][row], board[1][row], board[2][row])) {
                return getCell(0, row);
            }
        }
        return null;
    }

    /**
     * Initial method to check for row win.
     * @return True if there is a row win,
     * false if not.
     */
    private boolean checkRowForWin() {
        for(int row = 0; row < SIZE; row++) {
            if(checkMarks(board[0][row], board[1][row], board[2][row])) {
                return true;
            }
        }
        return false;
    }

    /**
     * New method to return the player who has a
     * diagonal win.
     * @return The player who has a diagonal win.
     * otherwise return null.
     */
    private CellContent checkDiagonalForWin() {
        if (checkDiaForWin()) {
            return getCell(1,1);
        } else {
            return null;
        }
    }

    /**
     * Initial method to check for diagonal win.
     * @return True if there is a diagonal win,
     * false if not.
     */
    private boolean checkDiaForWin() {
        return ((checkMarks(board[0][0], board[1][1], board[2][2])) || (checkMarks(board[0][2], board[1][1], board[2][0])));
    }

    /**
     * Check if three marks in order are the same.
     * @param c1 The first mark.
     * @param c2 The second mark.
     * @param c3 The third mark.
     * @return True if all marks are the same, false if not.
     */
    private boolean checkMarks(CellContent c1, CellContent c2, CellContent c3) {
        return(c1 != CellContent.Empty && c1 == c2 && c2 == c3);
    }

    /**
     * Prints the current board to the command line.
     */
    public void printBoard() {
        synchronized (System.out) {
            System.out.print("  ");
            for (int i = 0; i < SIZE; i++) {
                System.out.print(" " + i);
            }
            System.out.println();
            for (int y = 0; y < SIZE; y++) {
                System.out.print(y + " |");
                for (int x = 0; x < SIZE; x++) {
                    char playerMark;
                    switch (board[x][y]) {
                        case Local:
                            playerMark = 'X';
                            break;
                        case Remote:
                            playerMark = 'O';
                            break;
                        default:
                            playerMark = ' ';
                    }
                    System.out.print(ANSI_RED + playerMark + ANSI_RESET + "|");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static class Cell {
        public CellContent content;
        public int x;
        public int y;

        public Cell(int x, int y, CellContent content) {
            this.x = x;
            this.y = y;
            this.content = content;
        }
    }

    /**
     * Creates a list of all cells and their content.
     * @return A list of all cells and their content.
     */
    public List<Cell> getCellList() {
        ArrayList<Cell> result = new ArrayList<>();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                result.add(new Cell(x, y, getCell(x, y)));
            }
        }
        return result;
    }

    /**
     * Clones the current Tic-Tac-Toe board and returns it.
     * @return A clone of the current Tic-Tac-Toe board.
     */
    public TicTacToeBoard clone() {
        TicTacToeBoard clone = new TicTacToeBoard();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y<SIZE; y++) {
                clone.board[x][y] = this.board[x][y];
            }
        }
        return clone;
    }
}
