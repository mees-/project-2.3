package application;

import framework.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingPlayer implements Player {
    private final BlockingQueue<GameResult> stateQueue = new ArrayBlockingQueue<>(1);

    private Player originalPlayer;
    public BlockingPlayer(Player originalPlayer) {
        this.originalPlayer = originalPlayer;
    }

    @Override
    public Move getNextMove(BoardInterface board) {
        return originalPlayer.getNextMove(board);
    }

    @Override
    public void onEnd(GameResult state) {
        originalPlayer.onEnd(state);
        try {
            stateQueue.put(state);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public GameResult waitForEnd() throws InterruptedException {
        return stateQueue.take();
    }
}
