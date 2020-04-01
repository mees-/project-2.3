package framework.player;

import framework.*;
import framework.player.Player;

import java.util.Random;

public class RandomMovePlayer implements Player {
    private Random rand = new Random();

    @Override
    public Move getNextMove(BoardInterface board) {
        int boardSize = 3;
//        synchronized (board) {
//            boardSize = board.getSize();
//        }
        Move move;
        do {
            move = new Move(GameState.LocalTurn,rand.nextInt(boardSize), rand.nextInt(boardSize));
        } while (board.getCell(move.getX(), move.getY()) != CellContent.Empty);
        return move;
    }

    @Override
    public void onEnd(GameResult state) {
        switch (state) {
            case Win:
                System.out.println("Yay I won!");
                break;
            case Loss:
                System.out.println("darn I lost!");
                break;
            case Draw:
                System.out.println("Oh, it's a draw...");
                break;
        }
    }
}