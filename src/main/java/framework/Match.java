package framework;

import framework.player.Player;
import framework.player.Players;
import tictactoe.Game;

public class Match {
    private String remoteUsername;
    private String localUsername;
    private GameType gameType;
    private GameState gameState;
    private GameInterface game;

    private Players players = new Players();

    public String getRemoteUsername() {
        return remoteUsername;
    }

    public void setRemoteUsername(String remoteUsername) {
        this.remoteUsername = remoteUsername;
    }

    public String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameInterface getGame() {
        return game;
    }

    public void setGame(GameInterface game) {
        this.game = game;
    }

    public Match(Player one, Player two) {
        players.one = one;
        players.two = two;
    }

    public void setupGame(GameType gameType) {
        switch (gameType) {
            case TicTacToe:
                game = new Game();
                break;
            case Reversi:
//                game = new Reversi();
                break;
        }
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
            Move move = playerToMove.getNextMove(game.getBoard());
            try {
                GameState newState = game.doMove(move);
                setGameState(newState);
            } catch (InvalidMoveException e) {
                continue;
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
