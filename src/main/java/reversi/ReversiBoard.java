package reversi;

import framework.*;

import java.util.HashSet;
import java.util.Set;

public class ReversiBoard extends BoardInterface {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[30m";
    public static final String ANSI_BLACK = "\u001B[37m";
    private static final char BLACK_DISC = '#';
    private static final char WHITE_DISC = 'o';
    private static final char EMPTY_CELL = '-';
    private static final char SUGGEST_DISC = 'x';

    private char playerColour;
    private char opponentColour;

//    private Set<Move> cachedValidMoves = null;

    private static final int BOARD_SIZE = 8;

    private CellContent[][] board;
    private int[][] valueBoard;

    public ReversiBoard() {
        init();
    }

    private void init(){
        valueBoard = new int[BOARD_SIZE][BOARD_SIZE];
        board = new CellContent[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                setCell(i, j, CellContent.Empty);
                valueBoard[i][j] = 0;
            }
        }
        //These values were found in a Stanford paper that is no longer available,
        //but can be viewed at https://www.youtube.com/watch?v=y7AKtWGOPAE&t=16m6s at 16 minutes and 6 seconds.
        //CORNERS
        valueBoard[0][0] = 161;
        valueBoard[0][7] = 161;
        valueBoard[7][0] = 161;
        valueBoard[7][7] = 161;
        //BUFFERS
        valueBoard[0][1] = -40;
        valueBoard[1][0] = -40;
        valueBoard[0][6] = -40;
        valueBoard[6][0] = -40;
        valueBoard[1][7] = -40;
        valueBoard[7][1] = -40;
        valueBoard[6][7] = -40;
        valueBoard[7][6] = -40;
        valueBoard[1][1] = -20;
        valueBoard[6][1] = -20;
        valueBoard[1][6] = -20;
        valueBoard[6][6] = -20;
        //EDGES
        valueBoard[0][2] = 10;
        valueBoard[0][3] = 5;
        valueBoard[0][4] = 5;
        valueBoard[0][5] = 10;
        valueBoard[2][0] = 10;
        valueBoard[3][0] = 5;
        valueBoard[4][0] = 5;
        valueBoard[5][0] = 10;
        valueBoard[2][7] = 10;
        valueBoard[3][7] = 5;
        valueBoard[4][7] = 5;
        valueBoard[5][7] = 10;
        valueBoard[7][2] = 10;
        valueBoard[7][3] = 5;
        valueBoard[7][4] = 5;
        valueBoard[7][5] = 10;
        //CENTER
        valueBoard[2][2] = 5;
        valueBoard[5][2] = 5;
        valueBoard[5][5] = 5;
        valueBoard[2][5] = 5;
        valueBoard[1][3] = -2;
        valueBoard[3][1] = -2;
        valueBoard[1][4] = -2;
        valueBoard[4][1] = -2;
        valueBoard[6][4] = -2;
        valueBoard[4][6] = -2;
        valueBoard[6][3] = -2;
        valueBoard[3][6] = -2;
    }

    public int[][] getValueBoard() {
        return valueBoard;
    }

    public CellContent getCell(int x, int y) {
        if((x >= 0 && x < BOARD_SIZE) && (y >= 0 && y < BOARD_SIZE)) {
            return board[x][y];
        }
        return null;
    }

    public void setCell(int x, int y, CellContent content) {
        board[x][y] = content;
    }

    public void reset() {
        init();
    }

    public int getSize() {
        return BOARD_SIZE;
    }

    public GameState getResult(Move move) {
        GameState state = move.getPlayer();
        int[] pieces = countPieces();
        if(checkForWin() == CellContent.Local) {
            return GameState.OneWin;
        }
        else if(checkForWin() == CellContent.Remote) {
            return GameState.TwoWin;
        }
        else if(pieces[0] == pieces[1] && !canMakeTurn(move.getPlayer())
                && !canMakeTurn(getOpposite(state))) {
            return GameState.Draw;
        } else if (!canMakeTurn(getOpposite(state))) {
            return state;
        }
        else if(state == GameState.TurnOne) {
            return GameState.TurnTwo;
        }
        else if(state == GameState.TurnTwo) {
            return GameState.TurnOne;
        }
        return null;
    }


    public int[] countPieces() {
        int[] pieces;
        // [0] = Local pieces
        // [1] = Remote pieces
        pieces = new int[2];

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getCell(i, j) == CellContent.Local) {
                    pieces[0]++;
                } else if (getCell(i, j) == CellContent.Remote) {
                    pieces[1]++;
                }
            }
        }
        return pieces;
    }

    public CellContent checkForWin() {
        if (!canMakeTurn(GameState.TurnOne) && !canMakeTurn(GameState.TurnTwo)) {
            int[] pieces = countPieces();
            if (pieces[0] > pieces[1]) {
                return CellContent.Local;
            } else if (pieces[1] > pieces[0]) {
                return CellContent.Remote;
            }
        }
        return null;
    }

    public boolean canMakeTurn(GameState state) {
        if (getValidMoves(state).isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public Set<Move> getValidMoves(GameState state) {
//        if (cachedValidMoves != null) {
//            return cachedValidMoves;
//        }
        HashSet<Move> set = new HashSet<>();

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getCell(i, j) == CellContent.Empty) {
                    boolean ww = validMove(state, i, j, 0, -1);
                    boolean sw = validMove(state, i, j, 1, -1);
                    boolean ss = validMove(state, i, j, 1, 0);
                    boolean se = validMove(state, i, j, 1, 1);
                    boolean ee = validMove(state, i, j, 0, 1);
                    boolean ne = validMove(state, i, j, -1, 1);
                    boolean nn = validMove(state, i, j, -1, 0);
                    boolean nw = validMove(state, i, j, -1, -1);

                    if (nn || ne || ee || se || ss || sw || ww || nw) {
                        set.add(new Move(state, i, j));
                    }
                }
            }
        }

//        cachedValidMoves = set;
        return set;
    }

//    public void invalidateCache() {
//        cachedValidMoves = null;
//    }

    /**
     * Check if the current position contains the opposite player's colour and if the line indicated by adding checkX to
     * currentX and checkY to currentY eventually ends in the player's own colour
     * @param state
     * @param currentX
     * @param currentY
     * @param checkX
     * @param checkY
     * @return boolean if move is valid.
     */
    public boolean validMove(GameState state, int currentX, int currentY, int checkX, int checkY) {
        CellContent opposite = getOpposite(state).toCellContent();

        if ((currentX + checkX < 0) || (currentX + checkX >= getSize() )) {
            return false;
        }
        if ((currentY + checkY < 0) || (currentY + checkY >= getSize() )) {
            return false;
        }
        if (getCell(currentX + checkX, currentY + checkY) != opposite) {
            return false;
        }
        if ((currentX + 2 * checkX < 0) || (currentX + 2 * checkX >= getSize() )) {
            return false;
        }
        if ((currentY + 2 * checkY < 0) || (currentY + 2 * checkY >= getSize() )) {
            return false;
        }
        return check_line_match(state, currentX + 2 * checkX, currentY + 2 * checkY, checkX, checkY);
    }

    /**
     * Check if there is the player's colour on the line starting at (checkX, checkY) or anywhere further by adding
     * nextX and nextY to (checkX, checkY)
     * @param state
     * @param newX
     * @param newY
     * @param nextX
     * @param nextY
     * @return boolean if move creates line match
     */
    public boolean check_line_match(GameState state, int newX, int newY, int nextX, int nextY) {
        CellContent player = state.toCellContent();
        if (getCell(newX, newY) == player) {
            return true;
        }
        if (getCell(newX, newY) == CellContent.Empty){
            return false;
        }
        if ((nextX + newX < 0) || (nextX + newX >= getSize() )) {
            return false;
        }
        if ((nextY + newY < 0) || (nextY + newY >= getSize() )) {
            return false;
        }
        return check_line_match(state, newX + nextX, newY + nextY, nextX, nextY);
    }

    public GameState getOpposite(GameState state) {
        return state.otherPlayer();
    }

    public void setStartingPlayer(GameState startingPlayer) {
//        this.startingPlayer = startingPlayer;
        // If player one; black; first
        if (startingPlayer == GameState.TurnOne) {
//            System.out.println(gameState.toString()+" is black.");
            setCell(3, 3, CellContent.Remote);
            setCell(4, 4, CellContent.Remote);
            setCell(3, 4, CellContent.Local);
            setCell(4, 3, CellContent.Local);
            playerColour = BLACK_DISC;
            opponentColour = WHITE_DISC;

            // If player two; white; second
        } else {
//            System.out.println(gameState.toString()+" is white.");
            setCell(3, 3, CellContent.Local);
            setCell(4, 4, CellContent.Local);
            setCell(3, 4, CellContent.Remote);
            setCell(4, 3, CellContent.Remote);
            playerColour = WHITE_DISC;
            opponentColour = BLACK_DISC;
        }
    }

    public void printBoard() {
        System.out.println(ANSI_WHITE + "  | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | ");
        System.out.println("-----------------------------------");
        for (int row = 0; row < getSize(); row++) {
            System.out.print(ANSI_WHITE+row + " | ");
            for (int col = 0; col < getSize(); col++) {

//                System.out.print(ANSI_RED + "("+row+", "+col+") ");
                CellContent a = getCell(row, col);
                // Player one
                if (playerColour == BLACK_DISC) {
                    if (a == CellContent.Local) {
                        int blackCount = 0;
                        System.out.print(ANSI_BLACK + playerColour + ANSI_RED + " | ");
                    } else if (a == CellContent.Remote) {
                        System.out.print(ANSI_WHITE + opponentColour + ANSI_RED + " | ");
//                    } else if (coordinates.contains(new Point(row, col))) {
//                        System.out.print(ANSI_RED + SUGGEST_DISC + " | ");
                    } else {
                        System.out.print(ANSI_RED + EMPTY_CELL + ANSI_RED + " | ");
                    }
                // Player two
                } else if (playerColour == WHITE_DISC) {
                    if (a == CellContent.Local) {
                        System.out.print(ANSI_WHITE + playerColour + ANSI_RED + " | ");
                    } else if (a == CellContent.Remote) {
                        System.out.print(ANSI_BLACK + opponentColour + ANSI_RED + " | ");
//                    } else if (getValidMoves().contains(new Point(row, col))) {
//                        System.out.print(ANSI_RED + SUGGEST_DISC + " | ");
                    } else {
                        System.out.print(ANSI_RED + EMPTY_CELL + ANSI_RED + " | ");
                    }
                }
            }
            System.out.println("");
            System.out.println(ANSI_WHITE+"---"+ANSI_RED+"--------------------------------");
        }
        System.out.println(ANSI_WHITE+"");
        System.out.println("Player one pieces: "+countPieces()[0]+" | Player two pieces: "+countPieces()[1]);
    }

    @Override
    public ReversiBoard clone() {
        ReversiBoard clone = new ReversiBoard();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                clone.board[row][col] = this.board[row][col];
            }
        }
        return clone;
    }

    public CellContent[][] getBoard() {
        return board;
    }
}
