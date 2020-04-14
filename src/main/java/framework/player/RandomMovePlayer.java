package framework.player;

import framework.*;
import java.util.Random;
import java.util.Set;

public class RandomMovePlayer extends Player {
    private static final Random usernameRand = new Random();
    private final Random rand = new Random();

    public RandomMovePlayer(String username, GameType gameType) {
        super(username, gameType);
    }

    public RandomMovePlayer(GameType gameType) {
        this("randomUsername " + usernameRand.nextInt(200), gameType);
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        if (hasForfeit) {
            return new ForfeitMove(turn);
        }
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
    public void forfeit() {
        hasForfeit = true;
    }

    @Override
    public void onEnd(GameResult state) {
        Player.logEnd(state);
    }
}