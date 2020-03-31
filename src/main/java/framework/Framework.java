package framework;

import connection.Connection;
import tictactoe.Game;

import java.io.IOException;

public class Framework {
    private GameInterface game;
    private State state;
    private Connection connection;
    private Players players = new Players();

    public Framework(Player localPlayer) {
        state = new State();
        try {
            connection = new Connection(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        players.local = localPlayer;
        players.remote = connection.getPlayer();
    }

    public State getState() {
        return state;
    }

    public int getBoardSize() {
        return game.getBoard().getSize();
    }

    public void startGame(GameType gameType) {
        switch (gameType) {
            case TicTacToe:
                game = new Game();
                break;
            case Reversi:
//                game = new Reversi();
                break;
        }
        game.setup();
        gameLoop();
    }

    private void gameLoop() {
        while (!state.getGameState().isEnd()) {
            Player playerToMove;
            switch (state.getGameState()) {
                case LocalTurn:
                    playerToMove = players.local;
                    break;
                case RemoteTurn:
                    playerToMove = players.remote;
                    break;
                default:
                    throw new RuntimeException("Really shouldn't be here!");
            }
            Move move = playerToMove.getNextMove(game.getBoard());
            try {
                GameState newState = game.doMove(move);
                state.setGameState(newState);
            } catch (InvalidMoveException e) {
                continue;
            } catch (InvalidTurnException e) {
                throw new RuntimeException(e);
            }
            if (move.getPlayer() == GameState.LocalTurn) {
                connection.sendMove(move);
            }
        }
        System.out.println("Game end: " + state.getGameState().toString());
        switch (state.getGameState()) {
            case Win:
                players.local.onWin();
                players.remote.onLoss();
                break;
            case Loss:
                players.local.onLoss();
                players.remote.onWin();
                break;
            case Draw:
                players.local.onDraw();
                players.remote.onDraw();
                break;
            default:
                throw new RuntimeException("Really shoulnd't be here 2");
        }
    }

    public synchronized void requestGameSync(GameType gameType) {
        connection.subscribe(gameType);
        try {
            wait();
            startGame(state.getGameType());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void login(String username) {
        state.setLocalUsername(username);
        connection.login(username);
    }

    public synchronized void notifyGameOffer(GameType gameType, String remoteUsername, GameState startingPlayer) {
        state.setRemoteUsername(remoteUsername);
        state.setGameState(startingPlayer);
        state.setGameType(gameType);
        notify();
    }

    public void close() throws IOException {
        connection.close();
    }
}
