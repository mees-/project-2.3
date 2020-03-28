package tictactoe;

import framework.BoardInterface;
import framework.InvalidMoveException;
import framework.CellContent;

public class Board implements BoardInterface
    {
    // Colored text for command line
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private CellContent[][] board;

    public Board() {
        board = new CellContent[3][3];
        reset();
    }

    @Override
    public CellContent getCell(int row, int col) {
        if((row >= 0 && row < 3) && (col >= 0 && col < 3)) {
            return board[row][col];
        }
        return null;
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public void reset() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = CellContent.Empty;
            }
        }
    }

    @Override
    public void setCell(int row, int col, CellContent cellContent) throws InvalidMoveException {
        if((row >= 0 && row < 3) && (col >= 0 && col < 3) && (board[row][col] == CellContent.Empty) && cellContent != CellContent.Empty) {
            board[row][col] = cellContent;
        }
        else {
            throw new InvalidMoveException("The move to set xPos: " + (row + 1) + " and yPos: " + (col + 1) + " to " + cellContent + " is invalid.");
        }
    }

    public boolean boardIsFull() {
        boolean isFull = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if(board[row][col] == CellContent.Empty) {
                    isFull = false;
                }
            }
        }
        return isFull;
    }

    public boolean checkForWin() {
        return checkRowForWin() || checkColForWin() || checkDiaForWin();
    }

    private boolean checkRowForWin() {
        for(int row = 0; row < 3; row++) {
            if(checkMarks(board[row][0], board[row][1], board[row][2])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColForWin() {
        for(int col = 0; col < 3; col++) {
            if(checkMarks(board[0][col], board[1][col], board[2][col])) {
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
        System.out.print("  ");
        for(int i = 1; i < 4; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int row = 0; row < 3; row++) {
            System.out.print((row + 1) + " |");
            for (int col = 0; col < 3; col++) {
                char playerMark;
                switch(board[row][col]) {
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
