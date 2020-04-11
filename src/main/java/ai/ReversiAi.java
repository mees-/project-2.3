package ai;

import framework.*;
import reversi.ReversiBoard;

import java.util.Set;

public class ReversiAi extends Ai {
    private static final int BOARD_SIZE = 8;

    public ReversiAi(String username) {
        super(username, GameType.Reversi);
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
        try {
            CellContent gameWin = ((ReversiBoard)board).checkForWin();
            if (gameWin == turn.toCellContent()) {
                return 1000;
            } else if (gameWin == turn.otherPlayer().toCellContent()) {
                return -1000;
            } else {
                return (((ReversiBoard)board).getValueBoard()[lastMove.getX()][lastMove.getY()]);
            }
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Move> getValidMoves(GameState state, BoardInterface board) {
        return ((ReversiBoard)board).getValidMoves(state);
    }

    @Override
    public GameState getTurnAfterMove(BoardInterface currentBoard, Move lastMove) {
        CellContent player;

        try {
            player = lastMove.getPlayer().toCellContent();
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
        if (((ReversiBoard)currentBoard).canMakeTurn(((ReversiBoard)currentBoard).getOpposite(lastMove.getPlayer()))) {
            return lastMove.getPlayer().otherPlayer();
        } else {
            return lastMove.getPlayer();
        }
    }
}
