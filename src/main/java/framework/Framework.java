package framework;

import connection.Connection;
import tictactoe.Game;

import java.io.IOException;

public class Framework {
    private GameInterface game;
    private State state;
    private Connection connection;

    public Framework() {
        state = new State();
        try {
            connection = new Connection(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameState move(Move move) throws InvalidMoveException {
        GameState result = game.doMove(move);

        if (move.getPlayer() == GameState.LocalTurn) {
            connection.sendMove(move);
        }

        switch (result) {
            case LocalTurn:
                notifyState(GameState.LocalTurn);
            case RemoteTurn:
                notifyState(GameState.RemoteTurn);
                break;
            case Draw:
            case Loss:
            case Win:
//              board.reset();
                break;
            default:
                System.out.println("Something went wrong");
        }
        return result;
    }

    public synchronized void waitTurn() {
        while (state.getGameState() == GameState.RemoteTurn || state.getGameState() == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public GameState moveSync(Move move) throws InvalidMoveException {
        waitTurn();
        return move(move);
    }

    public State getState() {
        return state;
    }

    private void startGame(GameType gameType) {
        switch (gameType) {
            case TicTacToe:
                game = new Game();
                break;
            case Reversi:
//                game = new Reversi();
                break;
        }

        state.setBoard(game.getBoard());
        game.start();
    }

    private void requestGame(GameType gameType) {
        connection.subscribe(gameType);
    }

    public synchronized void requestGameSync(GameType gameType) {
        requestGame(gameType);
        while (state.getRemoteUsername() == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void login(String username) {
        connection.login(username);
        state.setLocalUsername(username);
    }

    public synchronized void notifyGameOffer(GameType gameType, String remoteUsername) {
        state.setRemoteUsername(remoteUsername);
        startGame(gameType);
        notifyAll();
    }

    public synchronized void notifyState(GameState playerType) {
        state.setGameState(playerType);
        notifyAll();
    }

    public void close() throws IOException {
        connection.close();
    }
}
