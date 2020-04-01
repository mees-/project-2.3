package framework;

import connection.Connection;
import framework.player.Player;

import java.io.IOException;

public class Framework {
    private Match match;
    private Connection connection;

    public Framework(Player localPlayer, Connection connection) {
        match = new Match(localPlayer, connection.getPlayer());
        this.connection = connection;
        this.connection.setFramework(this);
    }

    public Match getMatch() {
        return match;
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

    public void login(String username) {
        match.setLocalUsername(username);
        connection.login(username);
    }

    public synchronized void notifyGameOffer(GameType gameType, String remoteUsername, GameState startingPlayer) {
        match.setRemoteUsername(remoteUsername);
        match.setGameState(startingPlayer);
        match.setGameType(gameType);
        match.setupGame(match.getGameType());
        notify();
    }

    public void close() throws IOException {
        connection.close();
    }
}
