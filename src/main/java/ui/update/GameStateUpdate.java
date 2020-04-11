package ui.update;

import framework.BoardInterface;
import framework.GameState;

public class GameStateUpdate {
    private BoardInterface board;
    private GameState gameState;

    public GameStateUpdate(BoardInterface board, GameState gameState) {
        this.board = board;
        this.gameState = gameState;
    }

    public BoardInterface getBoard() {
        return board;
    }

    public GameState getGameState() {
        return gameState;
    }
}

