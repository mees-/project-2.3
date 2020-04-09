package framework;

import connection.Connection;
import connection.commands.LoginCommand;
import connection.commands.LogoutCommand;
import connection.eventHandlers.EventPayload;
import connection.eventHandlers.EventType;
import framework.player.Player;
import reversi.ReversiGame;
import tictactoe.Game;

import java.io.IOException;

public class Framework {
    private Match match;
    private final Player localPlayer;
    private final Connection connection;

    public Framework(Player localPlayer, Connection connection) {
        this.localPlayer = localPlayer;
        this.connection = connection;
        this.connection.setFramework(this);
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
        match = new Match(game, localPlayer, remotePlayer);
        match.setGameState(startingPlayer);
        match.setupGame();
        notify();
    }

    public synchronized void handleEvent(EventPayload payload) {

    }

    public void close() throws IOException {
        connection.executeCommand(new LogoutCommand());
        connection.close();
    }
}
