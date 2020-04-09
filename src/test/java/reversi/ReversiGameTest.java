package reversi;

import static org.junit.jupiter.api.Assertions.*;

import framework.*;
import org.junit.jupiter.api.*;
import reversi.ReversiBoard;
import reversi.ReversiGame;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ReversiGameTest {

    static ReversiGame game;

    @BeforeAll
    static void initializeGame() { game = new ReversiGame(); }

    @Test
    @Order(2)
    void getBoard() {
        assertTrue(game.getBoard() instanceof ReversiBoard);
    }

    @Test
    @Order(3)
    void doMove() throws InvalidMoveException, InvalidTurnException {
        game.doMove(new Move(GameState.TurnOne, 5, 4));
        assertEquals(CellContent.Local, game.getBoard().getCell(5,4));
        assertEquals(CellContent.Empty, game.getBoard().getCell(7,7));

        InvalidMoveException e = assertThrows(
                InvalidMoveException.class,
                () -> game.doMove(new Move(GameState.TurnTwo, 5, 4)));
        assertEquals("The move to set xPos: 5 and yPos: 4 to " + CellContent.Remote + " is invalid.", e.getMessage());

        game.doMove(new Move(GameState.TurnTwo, 5, 4));
        assertEquals(CellContent.Remote, game.getBoard().getCell(5,4));

        InvalidTurnException e2 = assertThrows(
                InvalidTurnException.class,
                () -> game.doMove(new Move(GameState.TurnTwo, 2, 4)));
        assertEquals(GameState.TurnTwo + " took two turns in a row, only one is allowed.", e2.getMessage());

        InvalidMoveException e3 = assertThrows(
                InvalidMoveException.class,
                () -> game.doMove(new Move(GameState.TurnOne, 2, 0)));
        assertEquals("The move to set xPos: 2 and yPos: 0 to " + CellContent.Local + " is invalid.", e3.getMessage());
    }

    @Test
    @Order(4)
    void setup() {
//        game.setup();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                assertEquals(CellContent.Empty, game.getBoard().getCell(i,j));
            }
        }
    }

    @Test
    @Order(1)
    void getType() {
        assertEquals(GameType.Reversi, game.getType());
    }
}