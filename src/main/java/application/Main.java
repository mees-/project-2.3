package application;

import framework.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] argv) {
        System.out.println("Hello, world!");
        Random rand = new Random();
        Framework framework = new Framework();
        framework.login("test" + rand.nextInt(100));
        framework.requestGameSync(GameType.TicTacToe);


        ArrayList<Move> moves = new ArrayList<>();

        mainloop:
        while (true) {
            framework.waitTurn();
            synchronized (framework) {
                switch (framework.getState().getGameState()) {
                    case Win:
                    case Draw:
                    case Loss:
                        break mainloop;
                    default:
                        try {
                            framework.waitTurn();
                            Move move = new Move(GameState.LocalTurn, rand.nextInt(3), rand.nextInt(3));
                            framework.moveSync(move);
                            moves.add(move);
                        } catch (InvalidMoveException e) {
                            System.out.println("Tried to do invalid move, retrying.");
                            System.out.println(e.getMessage());
                        }

                }
            }

        }

        try {
            framework.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
