package connection;

import framework.BoardInterface;
import framework.Move;
import framework.Player;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPlayer extends Player {

    BlockingQueue<Move> move = new ArrayBlockingQueue<>(1);

    public synchronized void putMove(Move move){
        try {
            this.move.put(move);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getNextMove(BoardInterface board) {
        try {
            return move.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoss() {

    }

    @Override
    public void onWin() {

    }

    @Override
    public void onDraw() {

    }
}
