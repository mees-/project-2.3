package ai;

import framework.BoardInterface;
import framework.GameState;
import framework.InvalidMoveException;
import framework.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MoveTree {
    private BoardInterface board;
    private Move move;
    private Ai gameUtils;

    private int evaluation;

    private MoveTree parent;
    private ArrayList<MoveTree> children = new ArrayList<>();

    public MoveTree(Ai gameUtils, BoardInterface board, GameState startingPlayer) {
        this.gameUtils = gameUtils;
        this.board = board;
        this.move = new Move(startingPlayer.otherPlayer(), -1,-1);
    }

    public MoveTree(Ai gameUtils, MoveTree parent, Move move) {
        this.gameUtils = gameUtils;
        this.parent = parent;
        this.move = move;
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

    public synchronized boolean hasParent(MoveTree parent) {
        if (parent == this.parent) {
            return true;
        } else if (this.parent != null) {
            return this.parent.hasParent(parent);
        } else {
            return false;
        }
    }

    public synchronized void removeParent() {
        parent = null;
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

    public int getHeight() {
        if (children.size() == 0) {
            return 1;
        } else {
            return 1 + children.get(0).getHeight();
        }
    }
    public int getAvgHeight() {
        if (children.size() == 0) {
            return 1;
        } else {
            return 1 + getAvg(children.stream().map(child -> child.getAvgHeight()).collect(Collectors.toList()));
        }
    }

    public int getAvg(List<Integer> list) {
        int total = 0;
        for (int el : list) {
            total += el;
        }
        return total / list.size();
    }

    public int getDepth() {
        if (parent == null) {
            return 1;
        } else {
            return 1 + parent.getDepth();
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
                result.addAll(node.getLeaves(result));
            }
        }
        return result;
    }

    private ArrayList<MoveTree> getLeaves(ArrayList<MoveTree> result) {
        for (MoveTree node : children) {
            if (node.isLeaf()) {
                result.add(node);
            } else {
                result.addAll(node.getLeaves(result));
            }
        }
        return result;
    }
}
