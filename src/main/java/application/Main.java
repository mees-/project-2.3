package application;

import ai.ReversiAi;
import ai.TicTacToeAi;
import connection.Connection;
import framework.*;
import reversi.ReversiGame;
import tictactoe.Game;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static final ArrayList<GameState> results = new ArrayList<>();

    private static final ArrayList<Long> timeResults = new ArrayList<>();

    private static final int amountOfGames = 10;
    public static void main(String[] argv) throws IOException, InterruptedException {
        System.out.println("Hello, world!");
        for (int i = 0; i < amountOfGames; i++) {
            long start = System.nanoTime();
            Match match = new Match(new ReversiGame(), new ReversiAi("a"), new ReversiAi("b"));
            match.setupGame(GameState.TurnTwo);
            match.startSync();
            long end = System.nanoTime();
            timeResults.add(end - start);
        }
//        synchronized (results) {
//            results.wait();
//        }
//        System.out.println(results);
//        System.out.println("OneWin: " + results.stream().filter(r -> r == GameState.OneWin).count());
//        System.out.println("TwoWin: " + results.stream().filter(r -> r == GameState.TwoWin).count());
//        System.out.println("Draw: " + results.stream().filter(r -> r == GameState.Draw).count());
        long sum = timeResults.stream().reduce((long)0, (a, b) -> a + b);
        double average = (double) sum / amountOfGames;
        System.out.println("avg: " + average);

    }
}
