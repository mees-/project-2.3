package application;

import ai.TicTacToeAi;
import connection.Connection;
import framework.*;
import tictactoe.Game;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static final ArrayList<GameState> results = new ArrayList<>();

    private static final int amountOfGames = 10;
    public static void main(String[] argv) throws IOException, InterruptedException {
        System.out.println("Hello, world!");
        for (int i = 0; i < amountOfGames; i++) {
            Match match = new Match(new Game(),new TicTacToeAi("a"), new TicTacToeAi("b"));
            match.setupGame(i % 2 == 0 ? GameState.TurnOne : GameState.TurnTwo);
            match.onEnd(m -> {
                synchronized (results) {
                    results.add(m.getGameState());
                    if (results.size() == amountOfGames) {
                        results.notify();
                    } else {
                        System.out.println(results.size());
                    }
                }
            });
            match.startAsync();
        }
        synchronized (results) {
            results.wait();
        }
        System.out.println(results);
        System.out.println("OneWin: " + results.stream().filter(r -> r == GameState.OneWin).count());
        System.out.println("TwoWin: " + results.stream().filter(r -> r == GameState.TwoWin).count());
        System.out.println("Draw: " + results.stream().filter(r -> r == GameState.Draw).count());
    }
}
