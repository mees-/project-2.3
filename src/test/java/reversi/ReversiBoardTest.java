package reversi;

import static org.junit.jupiter.api.Assertions.*;

import framework.CellContent;
import framework.InvalidMoveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReversiBoardTest {

    static ReversiBoard board;

    @BeforeAll
    static void initializeBoard() {
        board = new ReversiBoard();
    }

    @Test
    @Order(4)
    void getCell() {
        assertEquals(CellContent.Local, board.getCell(0, 0));
        assertEquals(CellContent.Remote, board.getCell(1, 0));
        assertNull(board.getCell(8, 8));
    }

    @Test
    @Order(2)
    void getSize() {
        assertEquals(8, board.getSize());
    }

    @Test
    @Order(5)
    void reset() {
        board.reset();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                assertEquals(CellContent.Empty, board.getCell(row, col));
            }
        }
    }

    @Test
    @Order(3)
    void setCell() {
        board.setCell(0, 0, CellContent.Local);
        board.setCell(0, 1, CellContent.Remote);
        board.setCell(0, 2, CellContent.Local);
        board.setCell(1, 0, CellContent.Remote);
        board.setCell(1, 1, CellContent.Local);
        board.setCell(1, 2, CellContent.Remote);
        board.setCell(2, 0, CellContent.Local);
        board.setCell(2, 1, CellContent.Remote);
        /*
        Local = L
        Remote = R
        Empty = E

        L|R|L
        R|L|R
        L|R|E
         */
    }

    @Test
    @Order(6)
    void countPieces () {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (col < 4) {
                    board.setCell(row, col, CellContent.Local);
                } else {
                    board.setCell(row, col, CellContent.Remote);
                }
            }
        }
        assertTrue(board.countPieces()[0] == 32);
        assertTrue(board.countPieces()[1] == 32);
    }

//    @Order(7)
//    void boardIsFull() throws InvalidMoveException {
//        assertFalse(board.boardIsFull());
//
//        board.setCell(0, 0, CellContent.Local);
//        board.setCell(0, 1, CellContent.Remote);
//        board.setCell(0, 2, CellContent.Local);
//        board.setCell(1, 0, CellContent.Remote);
//        board.setCell(1, 1, CellContent.Remote);
//        board.setCell(1, 2, CellContent.Local);
//        board.setCell(2, 0, CellContent.Local);
//        board.setCell(2, 1, CellContent.Remote);
//        board.setCell(2, 2, CellContent.Remote);
//        /*
//        Local = L
//        Remote = R
//
//        L|R|L
//        R|R|L
//        L|R|L
//         */
//
//        assertTrue(board.boardIsFull());
//    }
//
//    @Test
//    @Order(1)
//    void checkForWin() {
//        assertFalse(board.checkForWin());
//    }
//
//    @Test
//    @Order(5)
//    void checkDiaForWin() {
//        assertTrue(board.checkForWin());
//    }
//
//    @Test
//    @Order(9)
//    void checkRowForWin() throws InvalidMoveException {
//        board.reset();
//
//        board.setCell(0, 0, CellContent.Local);
//        board.setCell(0, 1, CellContent.Local);
//        board.setCell(0, 2, CellContent.Local);
//
//        assertTrue(board.checkForWin());
//    }
//
//    @Test
//    @Order(8)
//    void checkColForWin() {
//        assertTrue(board.checkForWin());
//    }
}