package ai;

import framework.*;
import framework.player.Player;

import java.util.Set;
import java.util.concurrent.*;

public abstract class Ai extends Player {
    private MoveTree tree;

    private ExecutorService executor = Executors.newFixedThreadPool(8);

    public Ai(String username, GameType gameType) {
        super(username, gameType);
    }

    public abstract void applyMoveToBoard(Move move, BoardInterface board) throws InvalidMoveException;

    public abstract int analyzeMove(Move lastMove, BoardInterface board);

    public abstract Set<Move> getValidMoves(GameState state, BoardInterface board);

    public abstract GameState getTurnAfterMove(BoardInterface currentBoard, Move lastMove);

    private void mutliTrheadMinimax(MoveTree head) {
        Future[] results = new Future[head.getChildren().size()];
        int idx = 0;
        for (MoveTree child : head.getChildren()) {
            results[idx++] = executor.submit(() -> {

                child.setEvaluation(minimax(child, child.getHeight(), Integer.MIN_VALUE, Integer.MAX_VALUE));
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

    private int minimax(MoveTree position, int depth, int alpha, int beta) {
        if (depth == 0 || position.getChildren().size() == 0) {
            return analyzeMove(position.getMove(), position.getBoard());
        }
        boolean maximizingPlayer = getTurnAfterMove(position.getBoard(), position.getMove()) == turn;
        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;
            for (MoveTree node : position.getChildren()) {
                int evaluation = minimax(node, depth -1, alpha, beta);
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
                int evaluation = minimax(node, depth - 1, alpha, beta);
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
        if (hasForfeit) {
            return new ForfeitMove(turn);
        }
        if (tree == null ) {
            tree = new MoveTree(this, board, getDepth(), turn);
        } else {
            for (MoveTree child : tree.getChildren()) {
                if (child.getMove().equals(lastMove)) {
                    tree = child;
                    break;
                }
            }
            if (tree.getHeight() != getDepth()) {
                for (MoveTree leaf: tree.getLeaves()) {
                    leaf.buildTree(getDepth() - leaf.getDepth());
                }
            }
        }
        mutliTrheadMinimax(tree);
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
                return possible;
            }
        }
        throw new RuntimeException("Minimax returned an invalid move");
    }

    @Override
    public void onEnd(GameResult state) {
        Player.logEnd(state);
    }

    public void reset() {
        tree = null;
    }

    @Override
    public void forfeit() {
        hasForfeit = true;
    }
}
