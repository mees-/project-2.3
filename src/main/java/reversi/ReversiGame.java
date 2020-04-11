package reversi;
import framework.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class ReversiGame implements GameInterface {

    private static final int BOARD_SIZE = 8;

    private CellContent playerCell;

    private boolean AI = false;

    private ReversiBoard board;

    private Set<Move> validMoves;

    private GameState lastTurn;



    public ReversiGame() {
        init();
    }

    private void init() {
        board = new ReversiBoard();
    }



    @Override
    public BoardInterface getBoard() {
        return board;
    }

    @Override
    public GameState doMove(Move move) throws InvalidMoveException, InvalidTurnException {
        if(!validCurrentTurn(move.getPlayer())) {
            throw new InvalidTurnException(move.getPlayer() + " took two turns in a row, only one is allowed.");
        }
        CellContent player;
        try {
            player = move.getPlayer().toCellContent();
        } catch (GameState.InvalidOperationException e) {
            throw new RuntimeException(e);
        }
        validMoves = getBoard().getValidMoves(move.getPlayer());
//        System.out.println(validMoves.toString());
        if (validMoves.contains(move)) {
            board.setCell(move.getX(), move.getY(), player);
            flipDiscs(move, player);
        } else {
            throw new InvalidMoveException("The move to set xPos: "+move.getX()+" and yPos: "+move.getY()+" to "+player+" is invalid.");
        }

        if (board.canMakeTurn(board.getOpposite(move.getPlayer()))) {
            setLastTurn(move.getPlayer());
        }
//        printBoard();
        return board.getResult(move);
    }

    @Override
    public void setup(GameState startingPlayer) {
        board.reset();
        board.setStartingPlayer(startingPlayer);
        board.getValidMoves(startingPlayer);
    }

    public void flipDiscs(Move move, CellContent player) {
        int x = move.getX();
        int y = move.getY();

        flipLine(player, x, y, 0, -1); //ww
        flipLine(player, x, y, 1, -1); //sw
        flipLine(player, x, y, 1, 0); //ss
        flipLine(player, x, y, 1, 1); //se
        flipLine(player, x, y, 0, 1); //ee
        flipLine(player, x, y, -1, 1); //ne
        flipLine(player, x, y, -1, 0); //nn
        flipLine(player, x, y, -1, -1); //nw
    }

    public boolean flipLine(CellContent player, int currentX, int currentY, int checkX, int checkY) {

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
            if (flipLine(player, currentX + checkX, currentY + checkY, checkX, checkY)) {
                board.setCell(currentX + checkX, currentY + checkY, player);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public GameType getType() {
        return GameType.Reversi;
    }

    public void printBoard() {
        board.printBoard();
    }

    private boolean validCurrentTurn(GameState player) {
        if(lastTurn == null) {
            lastTurn = player;
            return true;
        }
        else {
            return lastTurn != player;
        }
    }

    private void setLastTurn(GameState player) {
        lastTurn = player;
    }
}
