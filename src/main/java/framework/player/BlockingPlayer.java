package framework.player;

import framework.*;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingPlayer extends Player {

    private static final int POLL_TIMEOUT_MS = 10;

    final ArrayBlockingQueue<Move> moveQueue = new ArrayBlockingQueue<>(1);

    public BlockingPlayer(String username, GameType gameType) {
        super(username, gameType);
    }

    public synchronized void putMove(Move move){
        try {
            this.moveQueue.put(move);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        Move move = null;
        while (move == null) {
            if (hasForfeit) {
                return new ForfeitMove(turn);
            }
            try {
                move = moveQueue.poll(POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (!possibleMoves.contains(move)) {
            for (Move possibleMove : possibleMoves) {
                if (move.equals(possibleMove)) {
                    return possibleMove;
                }
            }
            throw new RuntimeException("The move from the async source wasn't found in the possible moves");
        } else {
            return move;
        }
    }

    @Override
    public void forfeit() {
        hasForfeit = true;
    }

    @Override
    public void onEnd(GameResult state) {}


}
