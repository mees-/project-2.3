package reversi;

import framework.BoardInterface;
import framework.CellContent;
import framework.GameState;
import framework.Move;

import java.util.Collection;

public class Board implements BoardInterface {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK = "\u001B[30m";

    private CellContent[][] board;
    private int size;

    public Board(int size) {
        this.size = size;
        init();
    }

    private void init(){
        board = new CellContent[size][size];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                setCell(i, j, CellContent.Empty);
            }
        }
    }

    public CellContent getCell(int x, int y) {
        return board[x][y];
    }

    public void setCell(int x, int y, CellContent content) {
        board[x][y] = content;
    }

    public void reset() {
        init();
    }

    public int getSize() {
        return size;
    }

    @Override
    public Collection<Move> getValidMoves(GameState state) {
        return null;
    }

    public int[] countPieces() {
        int[] pieces;
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

    public void printBoard() {

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                System.out.print(ANSI_RED+ "("+i+", "+j+") ");
                CellContent a = getCell(i, j);
                if (a == CellContent.Local) {
                    System.out.print(ANSI_BLACK + a + ANSI_RED + " | ");
                } else if (a == CellContent.Remote) {
                    System.out.print(ANSI_WHITE + a + ANSI_RED + " | ");
                } else {
                    System.out.print(ANSI_RED + a + ANSI_RED + " | ");
                }

            }
            System.out.println("");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
        }
        System.out.println("");
//        System.out.println(board.getCell(3,3));
    }
}
