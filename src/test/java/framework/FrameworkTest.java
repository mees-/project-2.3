package framework;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FrameworkTest {

    static Framework framework;
    static String name;

    @BeforeAll
    static void initializeFramework() {
        framework = new Framework();
        Random rand = new Random();
        name = "test" + rand.nextInt(100);
        framework.login(name);
        framework.requestGameSync(GameType.TicTacToe);
    }

    @Test
    @Order(1)
    public void Move() {
        Move move = new Move(GameState.LocalTurn, 0, 1);
        Move move1 = new Move(GameState.LocalTurn, 1, 0);

        try {
            assertEquals(GameState.RemoteTurn, framework.move(move));
            assertEquals(GameState.LocalTurn, framework.move(move1));
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void State() {
        assertEquals(name, framework.getState().getLocalUsername());
        assertEquals(3, framework.getState().getBoard().getSize());
        assertEquals(GameState.RemoteTurn, framework.getState().getGameState());
    }
}
