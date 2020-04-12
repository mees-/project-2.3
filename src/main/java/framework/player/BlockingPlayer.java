package framework.player;

import framework.*;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class BlockingPlayer extends Player {

    final ArrayBlockingQueue<Move> move = new ArrayBlockingQueue<>(1);

    public BlockingPlayer(String username, GameType gameType) {
        super(username, gameType);
    }

    public synchronized void putMove(Move move){
        try {
            this.move.put(move);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        try {
            Move sourceMove = move.take();
            if (sourceMove instanceof ForfeitMove) {
                return sourceMove;
            }
            for (Move possibleMove : possibleMoves) {
                if (possibleMove.getX() == sourceMove.getX() && possibleMove.getY() == sourceMove.getY()) {
                    return possibleMove;
                }
            }
            throw new RuntimeException("The move from the async source wasn't found in the possible moves");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnd(GameResult state) {}


}
