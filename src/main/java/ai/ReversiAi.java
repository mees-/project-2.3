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
        CellContent player = move.getPlayer().toCellContent();
        board.setCell(move.getX(), move.getY(), player);
        flipDiscs(move, player, board);
    }

    public void flipDiscs(Move move, CellContent player, BoardInterface board) throws InvalidMoveException {
        int x = move.getX();
        int y = move.getY();

        flipLine(player, board, x, y, 0, -1); //ww
        flipLine(player, board, x, y, 1, -1); //sw
        flipLine(player, board, x, y, 1, 0); //ss
        flipLine(player, board, x, y, 1, 1); //se
        flipLine(player, board, x, y, 0, 1); //ee
        flipLine(player, board, x, y, -1, 1); //ne
        flipLine(player, board, x, y, -1, 0); //nn
        flipLine(player, board, x, y, -1, -1); //nw
    }

    public boolean flipLine(CellContent player, BoardInterface board, int currentX, int currentY, int checkX, int checkY) throws InvalidMoveException {

        if ((currentX + checkX < 0) || (currentX + checkX >= board.getSize() )) {
            return false;
        }
        if ((currentY + checkY < 0) || (currentY + checkY >= board.getSize() )) {
            return false;
        }
        if (board.getCell(currentX + checkX, currentY + checkY) == CellContent.Empty) {
            return false;
        }
        if (board.getCell(currentX + checkX, currentY + checkY) == player) {
            return true;
        } else {
            if (flipLine(player, board, currentX + checkX, currentY + checkY, checkX, checkY)) {
                board.setCell(currentX + checkX, currentY + checkY, player);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int analyzeMove(Move lastMove, BoardInterface board) {
        CellContent gameWin = ((ReversiBoard)board).checkForWin();
        if (gameWin == turn.toCellContent()) {
            return 1000;
        } else if (gameWin == turn.otherPlayer().toCellContent()) {
            return -1000;
        } else if (!((ReversiBoard)board).canMakeTurn(turn.otherPlayer())) {
            return 50 + (((ReversiBoard) board).getValueBoard()[lastMove.getX()][lastMove.getY()]);
        } else if (!((ReversiBoard)board).canMakeTurn(turn)) {
            return -50 + (((ReversiBoard) board).getValueBoard()[lastMove.getX()][lastMove.getY()]);
        } else {
            return (((ReversiBoard)board).getValueBoard()[lastMove.getX()][lastMove.getY()]);
        }
    }

    @Override
    public Set<Move> getValidMoves(GameState state, BoardInterface board) {
        return board.getValidMoves(state);
    }

    @Override
    public GameState getTurnAfterMove(BoardInterface board, Move lastMove) {
        if (((ReversiBoard) board).canMakeTurn(lastMove.getPlayer().otherPlayer())) {
            return lastMove.getPlayer().otherPlayer();
        } else {
            return lastMove.getPlayer();
        }
    }

    @Override
    public int getDepth() {
        return 7;
    }
}
