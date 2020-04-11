package ai;

import framework.*;
import framework.player.Player;
import tictactoe.Board;

import java.util.Set;

public abstract class Ai extends Player {
    private MoveTree tree;
    public Ai(String username) {
        super(username);
    }

    public abstract void applyMoveToBoard(Move move, BoardInterface board) throws InvalidMoveException;

    public abstract int analyzeMove(Move lastMove, BoardInterface board);

    public abstract Set<Move> getValidMoves(GameState state, BoardInterface board);

    public abstract GameState getTurnAfterMove(BoardInterface currentBoard, Move lastMove);



    private Move getBestMove(BoardInterface board, int depth) {
        tree = new MoveTree(this, board, depth, turn);
        return minimax();
    }

    public Move minimax () {
        minimax(tree, tree.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE);
        MoveTree best = tree.getChildren().get(0);
        for (int i = 1; i < tree.getChildren().size(); i++) {
            MoveTree current = tree.getChildren().get(i);
            if (current.getEvaluation() > best.getEvaluation()) {
                best = current;
            }
        }
        return best.getMove();
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



    @Override
    public Move getNextMove(BoardInterface board, Set<Move> possibleMoves) {
        Move move = getBestMove(board, 6);
        for (Move possible : possibleMoves) {
            if (possible.equals(move)) {
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
