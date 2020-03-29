package tictactoe;

import static org.junit.jupiter.api.Assertions.*;

import framework.CellContent;
import framework.InvalidMoveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BoardTest {

    static Board board;

    @BeforeAll
    static void initializeBoard() {
        board = new Board();
    }

    @Test
    @Order(4)
    void getCell() {
        assertEquals(CellContent.Local, board.getCell(0, 0));
        assertEquals(CellContent.Remote, board.getCell(1, 0));
        assertNull(board.getCell(4, 4));
    }

    @Test
    @Order(2)
    void getSize() {
        assertEquals(3, board.getSize());
    }

    @Test
    @Order(6)
    void reset() {
        board.reset();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(CellContent.Empty, board.getCell(row, col));
            }
        }
    }

    @Test
    @Order(3)
    void setCell() throws InvalidMoveException {
        InvalidMoveException e = assertThrows(
                InvalidMoveException.class,
                () -> board.setCell(0, 0, CellContent.Empty));
        assertEquals("The move to set xPos: 1 and yPos: 1 to Empty is invalid.", e.getMessage());

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

        InvalidMoveException e2 = assertThrows(
                InvalidMoveException.class,
                () -> board.setCell(0, 0, CellContent.Local));
        assertEquals("The move to set xPos: 1 and yPos: 1 to Local is invalid.", e2.getMessage());

        InvalidMoveException e3 = assertThrows(
                InvalidMoveException.class,
                () -> board.setCell(3, 3, CellContent.Local));
        assertEquals("The move to set xPos: 4 and yPos: 4 to Local is invalid.", e3.getMessage());
    }

    @Test
    @Order(7)
    void boardIsFull() throws InvalidMoveException {
        assertFalse(board.boardIsFull());

        board.setCell(0, 0, CellContent.Local);
        board.setCell(0, 1, CellContent.Remote);
        board.setCell(0, 2, CellContent.Local);
        board.setCell(1, 0, CellContent.Remote);
        board.setCell(1, 1, CellContent.Remote);
        board.setCell(1, 2, CellContent.Local);
        board.setCell(2, 0, CellContent.Local);
        board.setCell(2, 1, CellContent.Remote);
        board.setCell(2, 2, CellContent.Remote);
        /*
        Local = L
        Remote = R

        L|R|L
        R|R|L
        L|R|L
         */

        assertTrue(board.boardIsFull());
    }

    @Test
    @Order(1)
    void checkForWin() {
        assertFalse(board.checkForWin());
    }

    @Test
    @Order(5)
    void checkDiaForWin() {
        assertTrue(board.checkForWin());
    }

    @Test
    @Order(8)
    void checkRowForWin() {
        assertTrue(board.checkForWin());
    }

    @Test
    @Order(9)
    void checkColForWin() {
        assertTrue(board.checkForWin());
    }
}