package framework.player;

import framework.*;

import java.util.Set;

public abstract class Player {
    private String username;
    protected GameState turn;
    protected GameType gameType;

    public Player(String username, GameType gameType) {
        if (username == null) {
            throw new RuntimeException("Username can't be null");
        }
        this.username = username;
        this.gameType = gameType;
    }

    public abstract Move getNextMove(BoardInterface board, Set<Move> possibleMoves, Move lastMove);

    public abstract void onEnd(GameResult state);

    public String getUsername() {
        return username;
    }

    public GameState getTurn() {
        return turn;
    }

    public void setTurn(GameState turn) {
        this.turn = turn;
    }

    public GameType getGameType() {
        return gameType;
    }

    public static void logEnd(GameResult state) {
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
