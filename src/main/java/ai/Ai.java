package ai;

import framework.*;
import framework.player.Player;

import java.util.Set;

public abstract class Ai extends Player {
    private MoveTree tree;

    public Ai(String username, GameType gameType) {
        super(username, gameType);
    }

    public abstract void applyMoveToBoard(Move move, BoardInterface board) throws InvalidMoveException;

    public abstract int analyzeMove(Move lastMove, BoardInterface board);

    public abstract Set<Move> getValidMoves(GameState state, BoardInterface board);

    public abstract GameState getTurnAfterMove(BoardInterface currentBoard, Move lastMove);

    public MoveTree getBestNode() {
        minimax(tree, tree.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE);
        MoveTree best = tree.getChildren().get(0);
        for (int i = 1; i < tree.getChildren().size(); i++) {
            MoveTree current = tree.getChildren().get(i);
            if (current.getEvaluation() > best.getEvaluation()) {
                best = current;
            }
        }
        return best;
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
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        // this is never called since we override the other overload
        return null;
    }

    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove) {
        if (tree == null ) {
            tree = new MoveTree(this, board, getDepth(), turn);
        } else {
            for (MoveTree child : tree.getChildren()) {
                if (child.getMove().equals(lastMove)) {
                    tree = child;
                    break;
                }
            }
            if (tree.getDepth() != getDepth()) {
                for (MoveTree leaf: tree.getLeaves()) {
                    leaf.buildTree(getDepth() - tree.getDepth());
                }
            }
        }
        MoveTree node = getBestNode();
        tree = node;
        for (Move possible : possibleMoves) {
            if (possible.equals(node.getMove())) {
                return possible;
            }
        }
        throw new RuntimeException("Minimax returned an invalid move");
    }

    @Override
    public void onEnd(GameResult state) {
        Player.logEnd(state);
    }
}
