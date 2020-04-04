package framework.player;

import framework.BoardInterface;
import framework.GameResult;
import framework.Move;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingPlayer extends Player {

    final BlockingQueue<Move> move = new ArrayBlockingQueue<>(1);

    public BlockingPlayer(String username) {
        super(username);
    }

    public synchronized void putMove(Move move){
        try {
            this.move.put(move);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        try {
            return move.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnd(GameResult state) {}


}
