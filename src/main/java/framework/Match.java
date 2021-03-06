package framework;

import framework.player.ComposablePlayer;
import framework.player.NoopComposer;
import framework.player.Player;
import framework.player.Players;
import gui.update.GameStateUpdate;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class Match {
    private static boolean printDebug = false;

    private GameState gameState;
    private GameInterface game;
    private LinkedBlockingQueue<GameStateUpdate> gameStateUpdates= new LinkedBlockingQueue<>();
    private Thread thread = new Thread(this::gameLoop);
    private Move lastMove = null;

    private Consumer<Match> onEnd;

    private Players<ComposablePlayer> players = new Players<>();

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
        players.one = new NoopComposer(one);
        players.two = new NoopComposer(two);

        players.one.setTurn(GameState.TurnOne);
        players.two.setTurn(GameState.TurnTwo);

        String printBoardEnv = System.getenv("GAME_MATCH_DEBUG");
        if (printBoardEnv != null) {
            printDebug = true;
        }
    }

    public void setupGame(GameState startingPlayer) {
        setGameState(startingPlayer);
        players.one.setStart(getGameState());
        players.two.setStart(getGameState());
        game.setup(gameState);
        try {
            gameStateUpdates.put(new GameStateUpdate(getGame().getBoard().clone(), getGameState()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void startAsync() {
//        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public void startSync() {
        gameLoop();
    }

    private void gameLoop() {
        while (!getGameState().isEnd()) {
            try {
                gameStateUpdates.put(new GameStateUpdate(getGame().getBoard().clone(), getGameState()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
            Move move = playerToMove.getNextMove(game.getBoard(), Collections.unmodifiableSet(possibleMoves), lastMove);
            if (move instanceof ForfeitMove) {
                setGameState(playerToMove.getTurn().otherPlayer().toWin());
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
                lastMove = move;
                gameStateUpdates.put(new GameStateUpdate(getGame().getBoard().clone(), getGameState()));
                if (printDebug) {
                    System.out.println(getGame().getBoard().toString());
                }
            } catch (InvalidMoveException | InvalidTurnException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (printDebug) {
            System.out.println("Game end: " + getGameState().toString());
        }
        try {
            gameStateUpdates.put(new GameStateUpdate(getGame().getBoard().clone(), getGameState()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        if (onEnd != null) {
            onEnd.accept(this);
        }
    }

    public Players<ComposablePlayer> getPlayers() {
        return players;
    }

    public synchronized ComposablePlayer getCurrentPlayer() {
        switch (getGameState()) {
            case TurnOne:
            case OneWin:
                return players.one;
            case TurnTwo:
            case TwoWin:
                return players.two;
            default:
                return null;
        }
    }

    public GameStateUpdate getGameUpdate() throws InterruptedException {
        return gameStateUpdates.take();
    }

    public void waitForEnd() throws InterruptedException {
        thread.join();
    }

    public void onEnd(Consumer<Match> fn) {
        onEnd = fn;
    }
}
