package tictactoe;

import framework.*;

import java.util.HashSet;
import java.util.Set;

public class Board implements BoardInterface
    {
    // Colored text for command line
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private final CellContent[][] board;

    public Board() {
        board = new CellContent[3][3];
        reset();
    }

    @Override
    public CellContent getCell(int x, int y) {
        if((x >= 0 && x < 3) && (y >= 0 && y < 3)) {
            return board[x][y];
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
    public void setCell(int x, int y, CellContent cellContent) throws InvalidMoveException {
        if((x >= 0 && x < 3) && (y >= 0 && y < 3) && (board[x][y] == CellContent.Empty) && cellContent != CellContent.Empty) {
            board[x][y] = cellContent;
        }
        else {
            throw new InvalidMoveException("The move to set yPos: " + (y + 1) + " and xPos: " + (x + 1) + " to " + cellContent + " is invalid.");
        }
    }

    public boolean boardIsFull() {
        boolean isFull = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
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

    private boolean checkColForWin() {
        for(int col = 0; col < 3; col++) {
            if(checkMarks(board[col][0], board[col][1], board[col][2])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRowForWin() {
        for(int row = 0; row < 3; row++) {
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
            for (int i = 1; i < 4; i++) {
                System.out.print(" " + i);
            }
            System.out.println();
            for (int row = 0; row < 3; row++) {
                System.out.print((row + 1) + " |");
                for (int col = 0; col < 3; col++) {
                    char playerMark;
                    switch (board[row][col]) {
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

        public Set<Move> getValidMoves(GameState state) {
            HashSet<Move> result = new HashSet<>();
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    if (getCell(x, y) == CellContent.Empty) {
                        result.add(new Move(state, x, y));
                    }
                }
            }
            return result;
        }
    }
