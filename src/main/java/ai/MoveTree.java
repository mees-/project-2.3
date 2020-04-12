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
        this.move = new Move(startingPlayer.otherPlayer(), -1,-1);
        buildTree(startingPlayer, depth);
    }

    public MoveTree(Ai gameUtils, MoveTree parent, int depth, Move move) {
        this.gameUtils = gameUtils;
        this.parent = parent;
        this.move = move;
        buildTree(gameUtils.getTurnAfterMove(getBoard(), getMove()), depth);
    }

    private void buildTree(GameState nextTurn, int depth) {
        if (depth > 1) {
            Iterable<Move> moves = gameUtils.getValidMoves(nextTurn, getBoard());
            for (Move move : moves) {
                this.children.add(new MoveTree(gameUtils, this, depth-1, move));
            }
        }
    }

    public void buildTree(int depth) {
        buildTree(gameUtils.getTurnAfterMove(getBoard(), getMove()), depth);
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

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public ArrayList<MoveTree> getLeaves() {
        ArrayList<MoveTree> result = new ArrayList<>();
        for (MoveTree node : children) {
            if (node.isLeaf()) {
                result.add(node);
            } else {
                result.addAll(node.getLeaves());
            }
        }
        return result;
    }
}
