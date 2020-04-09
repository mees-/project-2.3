package reversi;

import framework.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class ReversiBoard extends BoardInterface {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK = "\u001B[30m";

    private static final int BOARD_SIZE = 8;

    ArrayList<Point> coordinates = new ArrayList<Point>();

    private CellContent[][] board;

    public ReversiBoard() {
        init();
    }

    private void init(){
        board = new CellContent[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                setCell(i, j, CellContent.Empty);
            }
        }
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
        CellContent player;
        try {
            player = move.getPlayer().toCellContent();
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
        int[] pieces = countPieces();
        if(checkForWin() == CellContent.Local) {
            return GameState.OneWin;
        }
        else if(checkForWin() == CellContent.Remote) {
            return GameState.TwoWin;
        }
        else if(pieces[0] == pieces[1] && !canMakeTurn(player)
                && !canMakeTurn(getOpposite(player))) {
            return GameState.Draw;
        }
        else if(move.getPlayer() == GameState.TurnOne) {
            return GameState.TurnTwo;
        }
        else if(move.getPlayer() == GameState.TurnTwo) {
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
        if (!canMakeTurn(CellContent.Local) && canMakeTurn(CellContent.Remote)) {
            int[] pieces = countPieces();
            if (pieces[0] > pieces[1]) {
                return CellContent.Local;
            } else if (pieces[1] > pieces[0]) {
                return CellContent.Remote;
            }
        }
        return null;
    }

    public boolean canMakeTurn(CellContent player) {
        validMovesOverview(player);
        if (getSuggestionsList().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @param playerCell
     * @return Overview of valid moves
     */
    public void validMovesOverview(CellContent playerCell) {
        resetSuggestionsList();

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getCell(i, j) == CellContent.Empty) {
                    boolean ww = validMove(playerCell, i, j, 0, -1);
                    boolean sw = validMove(playerCell, i, j, 1, -1);
                    boolean ss = validMove(playerCell, i, j, 1, 0);
                    boolean se = validMove(playerCell, i, j, 1, 1);
                    boolean ee = validMove(playerCell, i, j, 0, 1);
                    boolean ne = validMove(playerCell, i, j, -1, 1);
                    boolean nn = validMove(playerCell, i, j, -1, 0);
                    boolean nw = validMove(playerCell, i, j, -1, -1);

                    if (nn || ne || ee || se || ss || sw || ww || nw) {
                        addSuggestionsList(i, j);
                    }
                }
            }
        }
    }

    private void addSuggestionsList(int x, int y) {
        coordinates.add(new Point(x, y));
    }

    private void resetSuggestionsList() {
        coordinates.clear();
    }

    public ArrayList<Point> getSuggestionsList() {
        return coordinates;
    }

    /**
     * Check if the current position contains the opposite player's colour and if the line indicated by adding checkX to
     * currentX and checkY to currentY eventually ends in the player's own colour
     * @param playerCell
     * @param currentX
     * @param currentY
     * @param checkX
     * @param checkY
     * @return boolean if move is valid.
     */
    public boolean validMove(CellContent playerCell, int currentX, int currentY, int checkX, int checkY) {
        CellContent opposite = getOpposite(playerCell);

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
        return check_line_match(playerCell, checkX, checkY, currentX + 2 * checkX, currentY + 2 * checkY);
    }

    /**
     * Check if there is the player's colour on the line starting at (checkX, checkY) or anywhere further by adding
     * nextX and nextY to (checkX, checkY)
     * @param playerCell
     * @param checkX
     * @param checkY
     * @param nextX
     * @param nextY
     * @return boolean if move creates line match
     */
    public boolean check_line_match(CellContent playerCell, int checkX, int checkY, int nextX, int nextY) {
        if (getCell(nextX, nextY) == playerCell) {
            return true;
        }
        if ((nextX + checkX < 0) || (nextX + checkX >= getSize() )) {
            return false;
        }
        if ((nextY + checkY < 0) || (nextY + checkY >= getSize() )) {
            return false;
        }
        return check_line_match(playerCell, checkX, checkY, checkX + nextX, checkY + nextY);
    }

    public CellContent getOpposite(CellContent playerCell) {
        CellContent opponent;
        if (playerCell == CellContent.Local) {
            opponent = CellContent.Remote;
        } else {
            opponent = CellContent.Local;
        }
        return opponent;
    }

    public void printBoard() {
        for (int row = 0; row < getSize(); row++) {
            for (int col = 0; col < getSize(); col++) {
                System.out.print(ANSI_RED + "("+row+", "+col+") ");
                CellContent a = getCell(row, col);
                if (a == CellContent.Local) {
                    System.out.print(ANSI_WHITE + a + ANSI_RED + " | ");
                } else if (a == CellContent.Remote) {
                    System.out.print(ANSI_BLACK + a + ANSI_RED + " | ");
                } else {
                    System.out.print(ANSI_RED + a + ANSI_RED + " | ");
                }
            }
            System.out.println("");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
        }
        System.out.println("");
    }

    @Override
    public Set<Move> getValidMoves(GameState state) {
        return null;
    }

    @Override
    public ReversiBoard clone() {
        ReversiBoard clone = new ReversiBoard();
        clone.board = this.board.clone();
        return clone;
    }
}
