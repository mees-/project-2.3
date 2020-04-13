package ai;

import framework.*;
import framework.player.Player;
import reversi.ReversiBoard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.*;

public abstract class Ai extends Player {
    private MoveTree tree;
    private Thread treeBuilderThread = new Thread(this::treeBuilder);
    private PausableExecutor executor = new PausableExecutor(8);

    // TEMP
    public LinkedList<Long> times = new LinkedList<>();
    public LinkedList<Integer> depths = new LinkedList<>();

    private static final long TREE_BUILD_TIME_MS = 5000;

    public Ai(String username, GameType gameType) {
        super(username, gameType);
    }

    public abstract void applyMoveToBoard(Move move, BoardInterface board) throws InvalidMoveException;

    public abstract int analyzeMove(Move lastMove, BoardInterface board);

    public abstract Set<Move> getValidMoves(GameState state, BoardInterface board);

    public GameState getTurnAfterMove(MoveTree node) {
        return getTurnAfterMove(node.getBoard(), node.getMove());
    }

    public abstract GameState getTurnAfterMove(BoardInterface board, Move lastMove);


    @Override
    public void setStart(GameState startingPlayer) {
        super.setStart(startingPlayer);
        ReversiBoard board = new ReversiBoard();
        board.setStartingPlayer(startingPlayer);
        tree = new MoveTree(this, board, startingPlayer);
        treeBuilderThread.start();
    }

    private void treeBuilder() {
        addDepthLevel(tree);
        ArrayList<MoveTree> leaves = tree.getLeaves();
        for (MoveTree leaf : leaves) {
            executor.submit(addLevelTask(leaf));
        }
    }

    public Runnable addLevelTask(MoveTree node) {
        return () -> {
            boolean hasParent;
            synchronized (this) {
                hasParent = node.hasParent(tree);
            }
            if (hasParent) {
                addDepthLevel(node);
                for (MoveTree child : node.getChildren()) {
                    executor.submit(this.addLevelTask(child));
                }
            }
        };
    }

    private void addDepthLevel(MoveTree parent) {
        for (Move move : getValidMoves(getTurnAfterMove(parent), parent.getBoard())) {
            parent.getChildren().add(new MoveTree(this, parent, move));
        }
    }

    private void mutliTrheadMinimax(MoveTree head) {
        Future[] results = new Future[head.getChildren().size()];
        int idx = 0;
        for (MoveTree child : head.getChildren()) {
            results[idx++] = executor.submit(() -> {

                child.setEvaluation(minimax(child, Integer.MIN_VALUE, Integer.MAX_VALUE));
            });
        }
        for (Future result : results) {
            try {
                result.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int minimax(MoveTree position, int alpha, int beta) {
        if (position.getChildren().size() == 0) {
            return analyzeMove(position.getMove(), position.getBoard());
        }
        boolean maximizingPlayer = getTurnAfterMove(position) == turn;
        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;
            for (MoveTree node : position.getChildren()) {
                int evaluation = minimax(node,  alpha, beta);
                node.setEvaluation(evaluation);
                best = Integer.max(best, evaluation);
                alpha = Integer.max(alpha, evaluation);
                if (beta <= alpha) {
                    break;
                }
            }
            return best;
        } else {
            int worst = Integer.MAX_VALUE;
            for (MoveTree node : position.getChildren()) {
                int evaluation = minimax(node,  alpha, beta);
                worst = Integer.min(worst, evaluation);
                beta = Integer.min(beta, evaluation);
                if (beta <= alpha) {
                    break;
                }
            }
            return worst;
        }
    }

    public abstract int getDepth();

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
//        tree.removeParent();
        if (hasForfeit) {
            return new ForfeitMove(turn);
        }
        if (lastMove != null && getTurnAfterMove(board, lastMove) != lastMove.getPlayer()) {
            synchronized (this) {
                boolean changed = lastMove == null;

                for (MoveTree child : tree.getChildren()) {
                    if (child.getMove().equals(lastMove)) {
                        tree = child;
                        tree.removeParent();
                        changed = true;
                        break;
                    }
                }
                if (!changed) {
                    throw new RuntimeException("Didn't change tree");
                }
            }
        }
        try {
            Thread.sleep(TREE_BUILD_TIME_MS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.pause();
//        executor.waitForTasks();
        long start = System.nanoTime();
        for (MoveTree child : tree.getChildren()) {
            child.setEvaluation(minimax(child, Integer.MIN_VALUE, Integer.MAX_VALUE));
        }
        long end = System.nanoTime();
        times.add(end-start);
        depths.add(tree.getAvgHeight());


        MoveTree best = tree.getChildren().get(0);
        for (int i = 1; i < tree.getChildren().size(); i++) {
            MoveTree current = tree.getChildren().get(i);
            if (current.getEvaluation() > best.getEvaluation()) {
                best = current;
            }
        }
        tree = best;
        tree.removeParent();

        for (Move possible : possibleMoves) {
            if (possible.equals(best.getMove())) {
                executor.resume();
                return possible;
            }
        }
        throw new RuntimeException("Minimax returned an invalid move");
    }

    @Override
    public void onEnd(GameResult state) {
        Player.logEnd(state);
        long total = 0;
        long max = Long.MIN_VALUE;
        for (long time : times) {
            max = Long.max(max, time);
            total += time;
        }
        long avg = total / times.size();
        System.out.println("Avg times: " + (double)avg / 1000000000);
        System.out.println("Max :" + (double)max / 1000000000);
        for (int depth : depths) {
            System.out.print(depth + " ");
        }
        System.out.println();
    }

    public void reset() {
        tree = null;
    }

    @Override
    public void forfeit() {
        hasForfeit = true;
    }
}
