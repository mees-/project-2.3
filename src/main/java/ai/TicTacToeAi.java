package ai;

import framework.*;
import tictactoe.Board;

import java.util.HashSet;
import java.util.Set;

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
        int score = 0;
        for (Board.Cell cell : ((Board)board).getCellList()) {
            if (cell.content != CellContent.Empty) {
                try {
                    score += cell.content == turn.toCellContent() ? 1 : -1;
                } catch (GameState.InvalidOperationException e) {
                    e.printStackTrace();
                }
            }
        }
        return score;
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
}
