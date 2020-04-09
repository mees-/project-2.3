package reversi;
import framework.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class ReversiGame implements GameInterface {

    private static final int BOARD_SIZE = 8;
    private static final char BLACK_DISC = '#';
    private static final char WHITE_DISC = 'o';

    private char playerColour;
    private char opponentColour;

    private CellContent playerCell;

    private boolean AI = false;

    private ReversiBoard board;

    private GameState lastTurn;



    public ReversiGame() {
        init();

        // If player one; black; first
        if (lastTurn != GameState.TurnTwo) {
//            System.out.println(gameState.toString()+" is black.");
            board.setCell(3, 3, CellContent.Remote);
            board.setCell(4, 4, CellContent.Remote);
            board.setCell(3, 4, CellContent.Local);
            board.setCell(4, 3, CellContent.Local);
            playerColour = BLACK_DISC;
            opponentColour = WHITE_DISC;

        // If player two; white; second
        } else {
//            System.out.println(gameState.toString()+" is white.");
            board.setCell(3, 3, CellContent.Local);
            board.setCell(4, 4, CellContent.Local);
            board.setCell(3, 4, CellContent.Remote);
            board.setCell(4, 3, CellContent.Remote);
            playerColour = WHITE_DISC;
            opponentColour = BLACK_DISC;
        }
    }

    private void init() {
        board = new ReversiBoard();
//        board.resetSuggestionsList();
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
        board.validMovesOverview(player);
        if (board.getSuggestionsList().contains(new Point(move.getX(), move.getY()))) {
            board.setCell(move.getX(), move.getY(), player);
            flipDiscs(move, player);
        } else {
            throw new InvalidMoveException("The move to set xPos: "+move.getX()+" and yPos: "+move.getY()+" to "+player+" is invalid.");
        }

        if (board.canMakeTurn(board.getOpposite(player))) {
            setLastTurn(move.getPlayer());
        }
        return board.getResult(move);
    }

    @Override
    public void setup() {
        board.reset();
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
