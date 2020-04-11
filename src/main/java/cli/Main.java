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

        CliPlayer cliPlayer = new CliPlayer(username, GameType.Reversi);
        LocalConnectedPlayer player = new LocalConnectedPlayer(cliPlayer, connection);
        Framework framework = new Framework(player, connection);
        framework.runGameSync(GameType.Reversi);
        framework.close();
    }

    private static class CliPlayer extends Player {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        public CliPlayer(String username, GameType gameType) {
            super(username, gameType);
        }

        @Override
        public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
            for (Move move : possibleMoves) {
                System.out.println("Possible move: " + move.toString());
            }
            try {
                System.out.print("Enter x: ");
                int x = Integer.parseInt(reader.readLine());
                System.out.print("Enter y: ");
                int y = Integer.parseInt(reader.readLine());
                for (Move move : possibleMoves) {
                    if (move.getY() == y && move.getX() == x) {
                        return move;
                    }
                }
                System.out.println("Not a valid move, try again");
                return getNextMove(board, possibleMoves);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("That's not a number, try again");
                return getNextMove(board, possibleMoves);
            }
        }

        @Override
        public void onEnd(GameResult state) {
            System.out.println("game end! " + state.toString());
        }
    }
}
