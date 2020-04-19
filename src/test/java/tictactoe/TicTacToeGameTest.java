package tictactoe;

import static org.junit.jupiter.api.Assertions.*;

import framework.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class TicTacToeGameTest {

    static TicTacToeGame game;

    @BeforeAll
    static void initializeGame() {
        game = new TicTacToeGame();
    }

    @Test
    @Order(2)
    void getBoard() {
        assertTrue(game.getBoard() instanceof TicTacToeBoard);
    }

    @Test
    @Order(3)
    void doMove() throws InvalidMoveException, InvalidTurnException {
        game.doMove(new Move(GameState.TurnOne, 0, 0));
        assertEquals(CellContent.Local, game.getBoard().getCell(0,0));
        assertEquals(CellContent.Empty, game.getBoard().getCell(0,1));

        InvalidMoveException e = assertThrows(
                InvalidMoveException.class,
                () -> game.doMove(new Move(GameState.TurnTwo, 0, 0)));
        assertEquals("The move to set yPos: 1 and xPos: 1 to " + CellContent.Remote + " is invalid.", e.getMessage());

        game.doMove(new Move(GameState.TurnTwo, 0, 1));
        assertEquals(CellContent.Remote, game.getBoard().getCell(0,1));

        InvalidTurnException e2 = assertThrows(
                InvalidTurnException.class,
                () -> game.doMove(new Move(GameState.TurnTwo, 0, 0)));
        assertEquals(GameState.TurnTwo + " took two turns in a row, only one is allowed.", e2.getMessage());

        InvalidMoveException e3 = assertThrows(
                InvalidMoveException.class,
                () -> game.doMove(new Move(GameState.TurnOne, 3, 3)));
        assertEquals("The move to set yPos: 4 and xPos: 4 to " + CellContent.Local + " is invalid.", e3.getMessage());
    }

    @Test
    @Order(4)
    void setup() {
        game.setup(GameState.Draw);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                assertEquals(CellContent.Empty, game.getBoard().getCell(i,j));
            }
        }
    }

    @Test
    @Order(1)
    void getType() {
        assertEquals(GameType.TicTacToe, game.getType());
    }
}