package framework.player;

import framework.*;
import java.util.Random;
import java.util.Set;

public class RandomMovePlayer extends Player {
    private static final Random usernameRand = new Random();
    private final Random rand = new Random();

    public RandomMovePlayer(String username) {
        super(username);
    }

    public RandomMovePlayer() {
        this("randomUsername " + usernameRand.nextInt(200));
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        int index = rand.nextInt(possibleMoves.size());
        for (Move move : possibleMoves) {
            if (index == 0) {
                return move;
            } else {
                index--;
            }
        }
        return null;
    }

    @Override
    public void onEnd(GameResult state) {
        switch (state) {
            case Win:
                System.out.println("Yay I won!");
                break;
            case Loss:
                System.out.println("darn I lost!");
                break;
            case Draw:
                System.out.println("Oh, it's a draw...");
                break;
        }
    }
}