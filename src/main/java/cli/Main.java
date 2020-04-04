package cli;

import connection.Connection;
import framework.*;
import framework.player.LocalConnectedPlayer;
import framework.player.Player;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static CliPlayer localPlayer;
    private static BlockingQueue<CliPlayer> playerQueue = new ArrayBlockingQueue<>(1);
    public static void main(String[] argv) throws IOException, InterruptedException {
        Connection connection = new Connection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter a username: ");
        String username = null;
        try {
            username = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CliPlayer cliPlayer = new CliPlayer(username);
        LocalConnectedPlayer player = new LocalConnectedPlayer(cliPlayer, connection);
        Framework framework = new Framework(player, connection);
        framework.login();
        framework.runGameSync(GameType.TicTacToe);
        framework.close();
    }

    private static class CliPlayer extends Player {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        public CliPlayer(String username) {
            super(username);
        }

        @Override
        public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
            System.out.print("Enter (simple) move: ");
            try {
                int position = Integer.parseInt(reader.readLine());
                Move move = Move.fromSimplePosition(GameState.LocalTurn, 3, position);
                return move;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("That's not a number");
                return getNextMove(board, possibleMoves);
            }
        }

        @Override
        public void onEnd(GameResult state) {
            System.out.println("game end! " + state.toString());
        }
    }
}
