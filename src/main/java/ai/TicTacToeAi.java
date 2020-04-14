package ai;

import framework.*;
import tictactoe.TicTacToeBoard;

import java.util.*;

public class TicTacToeAi extends Ai {
    public TicTacToeAi(String username) {
        super(username, GameType.TicTacToe);
    }

    @Override
    public void applyMoveToBoard(Move move, BoardInterface board) throws InvalidMoveException {
        if (board.getCell(move.getX(), move.getY()) == CellContent.Empty) {
            board.setCell(move.getX(), move.getY(), move.getPlayer().toCellContent());
        } else {
            throw new InvalidMoveException("That cell is already taken");
        }
    }

    @Override
    public int analyzeMove(Move lastMove, BoardInterface board) {
        // this method was found on
        // https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-2-evaluation-function/
        CellContent gameWin = ((TicTacToeBoard)board).checkForWinBetter();
        if (gameWin == turn.toCellContent()) {
            return 10;
        } else if (gameWin == turn.otherPlayer().toCellContent()) {
            return -10;
        } else {
            return 0;
        }
    }

    @Override
    public Set<Move> getValidMoves(GameState state, BoardInterface board) {
        if (((TicTacToeBoard)board).checkForWin()) {
            return new HashSet<>();
        }
        HashSet<Move> result = new HashSet<>();
        for (int x = 0; x < gameType.getBoardSize(); x++) {
            for (int y = 0; y < gameType.getBoardSize(); y++) {
                if (board.getCell(x, y) == CellContent.Empty) {
                    result.add(new Move(state, x, y));
                }
            }
        }
        return result;
    }

    @Override
    public GameState getTurnAfterMove(BoardInterface currentBoard, Move lastMove) {
        return lastMove.getPlayer().otherPlayer();
    }

    @Override
    public int getDepth() {
        return 9;
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
