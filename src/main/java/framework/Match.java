package framework;

import framework.player.Player;
import framework.player.Players;

import java.util.Collections;
import java.util.Set;

public class Match {
    private GameState gameState;
    private GameInterface game;
    private Thread thread = new Thread(this::gameLoop);

    private final Players players = new Players();

    public GameState getGameState() {
        return gameState;
    }

    private void setGameState(GameState gameState) {
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

    public void setupGame(GameState startingPlayer) {
        setGameState(startingPlayer);
        game.setup(gameState);
    }

    public void startAsync() {
        thread.start();
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
            if (move instanceof ForfeitMove) {
                setGameState(playerToMove.getTurn().otherPlayer().toWin());
                System.out.println(playerToMove.getUsername() + " forfeit");
                continue;
            }
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
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
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

    public void waitForEnd() throws InterruptedException {
        this.thread.join();
    }
}
