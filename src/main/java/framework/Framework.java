package framework;

import connection.Connection;
import framework.player.Player;
import reversi.ReversiGame;
import tictactoe.Game;

import java.io.IOException;

public class Framework {
    private Match match;
    private final Player localPlayerOne, localPlayerTwo;
    private final Connection connection;

    public Framework(Player localPlayer, Connection connection) {
        this.localPlayerOne = localPlayer;
        this.localPlayerTwo= null;
        this.connection = connection;
        this.connection.setFramework(this);
    }

    public Framework(Player localPlayerOne, Player localPlayerTwo) {
        this.localPlayerOne = localPlayerOne;
        this.localPlayerTwo = localPlayerTwo;
        this.connection = null;
    }

    public int getBoardSize() {
        return match.getGame().getBoard().getSize();
    }

    public synchronized void runGameSync(GameType gameType) {
        connection.subscribe(gameType);
        try {
            wait();

            match.gameLoop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        connection.login(localPlayerOne.getUsername());
    }
  
    public synchronized void notifyGameOffer(GameType gameType, Player remotePlayer, GameState startingPlayer) {
        GameInterface game = null;
      
        switch (gameType) {
            case TicTacToe:
                game = new Game();
                break;
            case Reversi:
                game = new ReversiGame();

                break;
        }
        match = new Match(game, localPlayerOne, remotePlayer);
        match.setGameState(startingPlayer);
        match.setupGame();
        notify();
    }
    public void close() throws IOException {
        connection.close();
    }

    public Player getLocalPlayerOne() {
        return localPlayerOne;
    }

    public Player getLocalPlayerTwo() {
        return localPlayerTwo;
    }

    public void clearMatch() {
        match = null;
    }

    public Match getMatch() {
        return match;
    }
}
