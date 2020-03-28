package tictactoe;

import framework.InvalidMoveException;

public class TicTacToeBoard
{
    // Colored text for command line
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private char[][] board;

    public TicTacToeBoard() {
        board = new char[3][3];
    }

    public void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
    }

    public void printBoard() {
        System.out.print("  ");
        for(int i = 0; i < 3; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int row = 0; row < 3; row++) {
            System.out.print(row + " |");
            for (int col = 0; col < 3; col++) {
                System.out.print(ANSI_RED + board[row][col] + ANSI_RESET + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void setPiece(int row, int col, char playerMark) throws InvalidMoveException {
        if((row >= 0 && row < 3) && (col >= 0 && col < 3)) {
            board[row][col] = playerMark;
        }
        else {
            throw new InvalidMoveException("The move " + row + " " + col + " is invalid.");
        }
    }
}
