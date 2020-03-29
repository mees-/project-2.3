package framework;

import connection.Connection;
import tictactoe.Game;

import java.io.IOException;

public class Framework {
    private GameInterface game;
    private State state;
    private Connection connection;

    public Framework() {
        state = new GameState();
//        try {
//            connection = new Connection(this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void move(Move move) {
        try {
            BoardInterface board = state.getBoard();
            MoveResult result = game.doMove(move);

            if (move.getPlayer() == PlayerType.Local) {
//                connection.sendMove(move);
            }

            switch (result) {
                case LocalTurn:
                    notifyTurn(PlayerType.Local);
                    break;
                case RemoteTurn:
                    notifyTurn(PlayerType.Remote);
                    break;
                case Draw:
                case Loss:
                case Win:
                    board.reset();
                    break;
                default:
                    System.out.println("Something went wrong");
            }
        } catch (InvalidMoveException e) {
            System.out.println(e);
        }
    }

    public State getState() {
        return state;
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

        state.setBoard(game.getBoard());
//        connection.login(state.getLocalUsername());
//        connection.subscribe(gameType);
        game.start();
    }

    public void notifyMove(Move move) {
        move(move);
    }

    public void notifyGameOffer(GameType gameType) {
        startGame(gameType);
    }

    public void notifyTurn(PlayerType playerType) {
        state.setTurn(playerType);
    }
}
