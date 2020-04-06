package ai;

import framework.BoardInterface;
import framework.GameState;
import framework.InvalidMoveException;
import framework.Move;

import java.util.ArrayList;

public class MoveTree {
    private BoardInterface board;
    private Move move;
    private Ai gameUtils;

    private int evaluation;

    private MoveTree parent;
    private ArrayList<MoveTree> children = new ArrayList<>();

    public MoveTree(Ai gameUtils, BoardInterface board, int depth, GameState startingPlayer) {
        this.gameUtils = gameUtils;
        this.board = board;
        try {
            this.move = new Move(startingPlayer.otherTurn(), -1,-1);
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
        buildTree(startingPlayer, depth);
    }

    public MoveTree(Ai gameUtils, MoveTree parent, int depth, Move move) {
        this.gameUtils = gameUtils;
        this.parent = parent;
        this.move = move;
        buildTree(gameUtils.getTurnAfterMove(getBoard(), getMove()), depth);
    }

    private void buildTree(GameState currentPlayer, int depth) {
        if (depth > 1) {
            Iterable<Move> moves = gameUtils.getValidMoves(currentPlayer, getBoard());
            for (Move move : moves) {
                this.children.add(new MoveTree(gameUtils, this, depth-1, move));
            }
        }
    }

    public BoardInterface getBoard() {
        if (board != null) {
            return board.clone();
        } else if (parent != null){
            board = parent.getBoard();
            try {
                gameUtils.applyMoveToBoard(getMove(), board);
                return board.clone();
            } catch (InvalidMoveException e) {
                throw new RuntimeException("Can't apply move to board");
            }
        } else {
            throw new RuntimeException("Node has neither a board nor a parent");
        }
    }

    public Move getMove() {
        return move;
    }

    public MoveTree getParent() {
        return parent;
    }

    public ArrayList<MoveTree> getChildren() {
        return children;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getDepth() {
        if (children.size() == 0) {
            return 1;
        } else {
            return 1 + children.get(0).getDepth();
        }
    }
}
