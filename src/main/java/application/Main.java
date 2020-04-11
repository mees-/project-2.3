package application;

import ai.TicTacToeAi;
import connection.Connection;
import framework.*;
import framework.player.*;

import java.io.IOException;
import java.util.Random;

public class Main {
    static Framework framework;
    public static void main(String[] argv) throws IOException, InterruptedException {
        System.out.println("Hello, world!");
        Connection connection = new Connection();
        LocalConnectedPlayer player = new LocalConnectedPlayer(new TicTacToeAi("mees" + (new Random()).nextInt(100)), connection);
        framework = new Framework(player, connection);
        connection.subscribe(GameType.TicTacToe);
        Match test = framework.getFutureMatch().get();
        test.waitForEnd();
        player.logout().waitForResolve();
        synchronized (System.out) {
            framework.close();
        }
    }
}
