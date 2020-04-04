package framework;

import framework.player.Player;
import framework.player.Players;

import java.util.Set;

public class Match {
    private GameState gameState;
    private GameInterface game;

    private final Players players = new Players();

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameInterface getGame() {
        return game;
    }

    public Match(GameInterface game, Player one, Player two) {
        this.game = game;
        players.one = one;
        players.two = two;
    }

    public void setupGame() {

        game.setup();
    }

    public void gameLoop() {
        while (!getGameState().isEnd()) {
            Player playerToMove;
            switch (getGameState()) {
                case LocalTurn:
                    playerToMove = players.one;
                    break;
                case RemoteTurn:
                    playerToMove = players.two;
                    break;
                default:
                    throw new RuntimeException("Really shouldn't be here!");
            }
            Set<Move> possibleMoves = game.getBoard().getValidMoves(gameState);
            Move move = playerToMove.getNextMove(game.getBoard(), possibleMoves);
            try {
                GameState newState = game.doMove(move);
                setGameState(newState);
            } catch (InvalidMoveException e) {
            } catch (InvalidTurnException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Game end: " + getGameState().toString());
        switch (getGameState()) {
            case OneWin:
                players.one.onEnd(GameResult.Win);
                players.two.onEnd(GameResult.Loss);
                break;
            case TwoWin:
                players.one.onEnd(GameResult.Loss);
                players.two.onEnd(GameResult.Win);
                break;
            case Draw:
                players.one.onEnd(GameResult.Draw);
                players.two.onEnd(GameResult.Draw);
                break;
            default:
                throw new RuntimeException("Really shoulnd't be here 2");
        }
    }
}
