package framework;

import framework.player.Player;
import framework.player.Players;
import ui.update.GameStateUpdate;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Match {
    private GameState gameState;
    private GameInterface game;
    private LinkedBlockingQueue<GameStateUpdate> gameStateUpdates= new LinkedBlockingQueue<>();

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

        players.one.setTurn(GameState.TurnOne);
        players.two.setTurn(GameState.TurnTwo);
    }

    public void setupGame() {
        game.setup(gameState);
        try {
            gameStateUpdates.put(new GameStateUpdate(getGame().getBoard().clone(), getGameState()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameLoop() {

        while (!getGameState().isEnd()) {
            Player playerToMove;
            switch (getGameState()) {
                case TurnOne:
                    playerToMove = players.one;
                    break;
                case TurnTwo:
                    playerToMove = players.two;
                    break;
                default:
                    throw new RuntimeException("Really shouldn't be here!");
            }
            Set<Move> possibleMoves = game.getBoard().getValidMoves(gameState);
            Move move = playerToMove.getNextMove(game.getBoard(), Collections.unmodifiableSet(possibleMoves));
            if (!possibleMoves.contains(move)) {
                System.err.println("Returned move is not in set of valid moves");
                switch (getGameState()) {
                    case TurnOne:
                        setGameState(GameState.TwoWin);
                        break;
                    case TurnTwo:
                        setGameState(GameState.OneWin);
                        break;
                }
                continue;
            }
            try {
                GameState newState = game.doMove(move);
                setGameState(newState);
                gameStateUpdates.put(new GameStateUpdate(getGame().getBoard().clone(), getGameState()));
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            } catch (InvalidTurnException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
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

    public Players getPlayers() {
        return players;
    }

    public GameStateUpdate getGameUpdate() throws InterruptedException {
        return gameStateUpdates.take();
    }
}
