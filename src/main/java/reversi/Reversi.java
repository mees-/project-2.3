package reversi;
import java.util.Scanner;

public class Reversi {
    public static final int BOARD_SIZE = 8;
    public static final char BLACK_DISC = '#';
    public static final char WHITE_DISC = 'o';
    public static final char SUGGEST_DISC = 'x';
    public static final char EMPTY_SPACE = '-';

    private boolean AI = false;

    private boolean whiteTurn = true;

    private char[][] board;
    private char[][] suggestions;

    private int whiteScore;
    private int blackScore;

    private char checkTile;


    public Reversi() {
        init();
    }

    private void init() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        suggestions = new char[BOARD_SIZE][BOARD_SIZE];
    }

    public void newGame() {
        resetBoard();
        whiteTurn = true;
//        getNextMove();
    }

    public void resetBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_SPACE;
            }
        }

        board[3][3] = WHITE_DISC;
        board[4][4] = WHITE_DISC;
        board[3][4] = BLACK_DISC;
        board[4][3] = BLACK_DISC;
    }

    /**
     * @param colour
     * @return Overview of valid moves
     */
    public char[][] validMovesOverview(char colour) {
        suggestions = board.clone();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY_SPACE) {
                    boolean nn = validMove(colour, i, j, 0, -1);
                    boolean ne = validMove(colour, i, j, 1, -1);
                    boolean ee = validMove(colour, i, j, 1, 0);
                    boolean se = validMove(colour, i, j, 1, 1);
                    boolean ss = validMove(colour, i, j, 0, 1);
                    boolean sw = validMove(colour, i, j, -1, 1);
                    boolean ww = validMove(colour, i, j, -1, 0);
                    boolean nw = validMove(colour, i, j, -1, -1);

                    if (nn || ne || ee || se || ss || sw || ww || nw) {
                        suggestions[i][j] = SUGGEST_DISC;
                    }
                }
            }
        }
        return suggestions;
    }

    /**
     * Check if the current position contains the opposite player's colour and if the line indicated by adding checkX to
     * currentX and checkY to currentY eventually ends in the player's own colour
     * @param colour
     * @param currentX
     * @param currentY
     * @param checkX
     * @param checkY
     * @return boolean if move is valid.
     */
    public boolean validMove(char colour, int currentX, int currentY, int checkX, int checkY) {
        char opposite;
        if (colour == BLACK_DISC) {
             opposite = WHITE_DISC;
        } else {
            opposite = BLACK_DISC;
        }

        if ((currentX + checkX < 0) || (currentX + checkX >= BOARD_SIZE )) {
            return false;
        }
        if ((currentY + checkY < 0) || (currentY + checkY >= BOARD_SIZE )) {
            return false;
        }
        if (board[currentX + checkX][currentY + checkY] != opposite) {
            return false;
        }
        if ((currentX + 2 * checkX < 0) || (currentX + 2 * checkX >= BOARD_SIZE )) {
            return false;
        }
        if ((currentY + 2 * checkY < 0) || (currentY + 2 * checkY >= BOARD_SIZE )) {
            return false;
        }
        return check_line_match(colour, checkX, checkY, currentX + 2 * checkX, currentY + 2 * checkY);
    }

    /**
     * Check if there is the player's colour on the line starting at (checkX, checkY) or anywhere further by adding
     * nextX and nextY to (checkX, checkY)
     * @param colour
     * @param checkX
     * @param checkY
     * @param nextX
     * @param nextY
     * @return boolean if move creates line match
     */
    public boolean check_line_match(char colour, int checkX, int checkY, int nextX, int nextY) {
//        System.out.println("nextX = "+nextX+"; nextY = "+nextY);
        if (board[nextX][nextY] == colour) {
            return true;
        }
        if ((nextX + checkX < 0) || (nextX + checkX >= BOARD_SIZE )) {
            return false;
        }
        if ((nextY + checkY < 0) || (nextY + checkY >= BOARD_SIZE )) {
            return false;
        }
        return check_line_match(colour, checkX, checkY, checkX + nextX, checkY + nextY);
    }

    public char[][] getBoard() {
        return board;
    }

    public char[][] getSuggestionsBoard() {
        return suggestions;
    }

    public void printBoard(char[][] boardType) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(boardType[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
