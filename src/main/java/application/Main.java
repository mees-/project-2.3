package application;

import connection.Connection;
import framework.*;
import framework.player.*;

import java.io.IOException;
import java.util.Random;

public class Main {
    static Framework framework;
    public static void main(String[] argv) throws IOException {
        System.out.println("Hello, world!");
        Connection connection = new Connection();
        Player player = new LocalConnectedPlayer(new RandomMovePlayer(), connection);
        framework = new Framework(player, connection);
        framework.login();
        framework.runGameSync(GameType.TicTacToe);
        framework.close();
    }
}
