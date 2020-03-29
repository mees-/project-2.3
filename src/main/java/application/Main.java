package application;

import framework.*;
import java.util.Random;

public class Main {
    public static void main(String[] argv) {
        System.out.println("Hello, world!");
        Framework framework = new Framework();
        State state = framework.getState();
        state.setLocalUsername("Test");
        framework.startGame(GameType.TicTacToe);

        Random rand = new Random();
        int x = 0, y = 0, i = 0;
        PlayerType type = null;
        while (i < 9) {
            x = rand.nextInt(3);
            y = rand.nextInt(3);

            type = i % 2 == 0 ? PlayerType.Local : PlayerType.Remote;

            Move move = new Move(type, x, y);
            framework.notifyMove(move);
            i++;
        }
    }
}
