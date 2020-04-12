package tictactoe;

import framework.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicTacToeBoard extends BoardInterface {
    // Colored text for command line
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final int SIZE = 3;

        private final CellContent[][] board;

    public TicTacToeBoard() {
        board = new CellContent[SIZE][SIZE];
        reset();
    }

    @Override
    public CellContent getCell(int x, int y) {
        if((x >= 0 && x < SIZE) && (y >= 0 && y < SIZE)) {
            return board[x][y];
        }
        return null;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public Set<Move> getValidMoves(GameState state) {
        HashSet<Move> result = new HashSet<>();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (getCell(x, y) == CellContent.Empty) {
                    result.add(new Move(state, x, y));
                }
            }
        }
        return result;
    }

    @Override
    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = CellContent.Empty;
            }
        }
    }

    @Override
    public void setCell(int x, int y, CellContent cellContent) throws InvalidMoveException {
        if((x >= 0 && x < SIZE) && (y >= 0 && y < SIZE) && (board[x][y] == CellContent.Empty) && cellContent != CellContent.Empty) {
            board[x][y] = cellContent;
        }
        else {
            throw new InvalidMoveException("The move to set yPos: " + (y + 1) + " and xPos: " + (x + 1) + " to " + cellContent + " is invalid.");
        }
    }

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

    public boolean checkForWin() {
        return checkRowForWin() || checkColForWin() || checkDiaForWin();
    }

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
    private CellContent checkXForWin() {
        for(int x = 0; x < SIZE; x++) {
            if(checkMarks(board[x][0], board[x][1], board[x][2])) {
                return getCell(x, 0);
            }
        }
        return null;
    }

    private CellContent checkYForWin() {
        for(int y = 0; y < SIZE; y++) {
            if(checkMarks(board[0][y], board[1][y], board[2][y])) {
                return getCell(0, y);
            }
        }
        return null;
    }

    private CellContent checkDiagonalForWin() {
        if (checkDiaForWin()) {
            return getCell(1,1);
        } else {
            return null;
        }
    }

    private boolean checkColForWin() {
        for(int col = 0; col < SIZE; col++) {
            if(checkMarks(board[col][0], board[col][1], board[col][2])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRowForWin() {
        for(int row = 0; row < SIZE; row++) {
            if(checkMarks(board[0][row], board[1][row], board[2][row])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiaForWin() {
        return ((checkMarks(board[0][0], board[1][1], board[2][2])) || (checkMarks(board[0][2], board[1][1], board[2][0])));
    }

    private boolean checkMarks(CellContent c1, CellContent c2, CellContent c3) {
        return(c1 != CellContent.Empty && c1 == c2 && c2 == c3);
    }

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

        public List<Cell> getCellList() {
            ArrayList<Cell> result = new ArrayList<>();
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    result.add(new Cell(x, y, getCell(x, y)));
                }
            }
            return result;
        }

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
