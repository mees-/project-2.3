package ai;

import framework.*;
import tictactoe.Board;

import java.util.*;

public class TicTacToeAi extends Ai {
    private static final int BOARD_SIZE = 3;

    public TicTacToeAi(String username) {
        super(username);
    }

    @Override
    public void applyMoveToBoard(Move move, BoardInterface board) throws InvalidMoveException {
        if (board.getCell(move.getX(), move.getY()) == CellContent.Empty) {
            try {
                board.setCell(move.getX(), move.getY(), move.getPlayer().toCellContent());
            } catch (GameState.InvalidOperationException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new InvalidMoveException("That cell is already taken");
        }
    }

    @Override
    public int analyzeMove(Move lastMove, BoardInterface board) {
        // this method was found on
        // https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-2-evaluation-function/
        try {
            CellContent gameWin = ((Board)board).checkForWinBetter();
            if (gameWin == turn.toCellContent()) {
                return 10;
            } else if (gameWin == turn.otherTurn().toCellContent()) {
                return -10;
            } else {
                return 0;
            }
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isMine(BoardInterface board, Collection<CoordinatePair> pairs) {
        for (CoordinatePair pair : pairs) {
            if (!isMine(board, pair)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCells(BoardInterface board, Collection<Board.Cell> cells) {
        for (Board.Cell cell : cells) {
            if (!checkCell(board, cell)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCell(BoardInterface board, Board.Cell cell) {
        return board.getCell(cell.x, cell.y) == cell.content;
     }

    private boolean isMine(BoardInterface board, CoordinatePair pair) {
        try {
            return board.getCell(pair.x, pair.y) == turn.toCellContent();
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Move> getValidMoves(GameState state, BoardInterface board) {
        if (((Board)board).checkForWin()) {
            return new HashSet<>();
        }
        HashSet<Move> result = new HashSet<>();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board.getCell(x, y) == CellContent.Empty) {
                    result.add(new Move(state, x, y));
                }
            }
        }
        return result;
    }

    @Override
    public GameState getTurnAfterMove(BoardInterface currentBoard, Move lastMove) {
        try {
            return lastMove.getPlayer().otherTurn();
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CoordinatePair {
        public final int x;
        public final int y;

        public CoordinatePair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
