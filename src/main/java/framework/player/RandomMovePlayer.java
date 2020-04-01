package framework.player;

import framework.*;
import framework.player.Player;

import java.util.Random;

public class RandomMovePlayer extends Player {
    private static final Random usernameRand = new Random();
    private final Random rand = new Random();

    public RandomMovePlayer(String username) {
        super(username);
    }

    public RandomMovePlayer() {
        this("randomUsername " + usernameRand.nextInt(200));
    }

    @Override
    public Move getNextMove(BoardInterface board) {
        int boardSize = board.getSize();
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