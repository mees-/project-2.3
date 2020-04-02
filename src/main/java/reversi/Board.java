package reversi;

import framework.BoardInterface;
import framework.CellContent;

public class Board implements BoardInterface {
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
}
