package framework;

import static org.junit.jupiter.api.Assertions.*;

import connection.Connection;
import framework.player.LocalConnectedPlayer;
import framework.player.Player;
import framework.player.RandomMovePlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FrameworkTest {

    static Framework framework;

    @BeforeAll
    static void initializeFramework() throws IOException {
        Connection connection = new Connection();
        Player player = new LocalConnectedPlayer(new RandomMovePlayer(GameType.TicTacToe), connection);
        framework = new Framework(player, connection);
        framework.runGameSync(GameType.TicTacToe);
        framework.close();
    }

    @Test
    @Order(1)
    public void State() {
        assertEquals(3, framework.getBoardSize());
    }
}
