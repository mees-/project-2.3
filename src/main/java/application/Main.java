package application;

import framework.*;

import java.io.IOException;
import java.util.Random;

public class Main {
    static Framework framework;
    public static void main(String[] argv) {
        System.out.println("Hello, world!");
        framework = new Framework(new LocalPlayer());
        framework.login("test" + (new Random()).nextInt(100));
        framework.requestGameSync(GameType.TicTacToe);
    }

    static class LocalPlayer extends Player {
        private Random rand = new Random();

        @Override
        public Move getNextMove(BoardInterface board) {
            Move move;
            do {
                move = new Move(GameState.LocalTurn,rand.nextInt(3), rand.nextInt(3));
            } while (board.getCell(move.getX(), move.getY()) != CellContent.Empty);
            return move;
        }

        @Override
        public void onLoss() {
            System.out.println("darn I lost!");
            try {
                framework.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onWin() {
            System.out.println("Yay I won!");
            try {
                framework.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDraw() {
            System.out.println("Oh, it's a draw...");
            try {
                framework.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
