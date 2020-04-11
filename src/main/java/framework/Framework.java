package framework;

import connection.Connection;
import connection.commands.LogoutCommand;
import connection.eventHandlers.EventPayload;
import connection.eventHandlers.MatchOfferHandler;
import framework.player.Player;
import reversi.ReversiGame;
import tictactoe.Game;

import java.io.IOException;

public class Framework {
    private Match match;
    private final Player localPlayer;
    private final Connection connection;
    private final Thread eventLoopThread = new Thread(this::eventLoop);
    private final Object matchMonitor = new Object();

    public Framework(Player localPlayer, Connection connection) {
        this.localPlayer = localPlayer;
        this.connection = connection;
        eventLoopThread.start();
    }

    public int getBoardSize() {
        return match.getGame().getBoard().getSize();
    }

    private void eventLoop() {
        while (true) {
            try {
                handleEvent(connection.getEvent());
            } catch (InterruptedException e) {
                System.out.println("Stopped eventloop");
            }
        }
    }

    private synchronized void handleEvent(EventPayload payload) {
        switch (payload.getType()) {
            case MatchOffer: {
                GameInterface game = null;
                MatchOfferHandler.MatchOffer matchOffer = (MatchOfferHandler.MatchOffer)payload;
                switch (matchOffer.getGameType()) {
                    case TicTacToe:
                        game = new Game();
                        break;
                    case Reversi:
                        game = new ReversiGame();
                        break;
                }
                match = new Match(game, localPlayer, matchOffer.getRemotePlayer());
                synchronized (match) {
                    synchronized (matchMonitor) {
                        this.matchMonitor.notifyAll();
                    }
                    match.setupGame(matchOffer.getStartingState());
                    match.startAsync();
                }
                break;
            }
        }
    }

    public void waitForMatch() throws InterruptedException {
        synchronized (matchMonitor) {
            matchMonitor.wait();
        }
    }
    public void close() throws IOException {
        connection.executeCommand(new LogoutCommand());
        connection.close();
        eventLoopThread.interrupt();
    }

    public Match getMatch() {
        return match;
    }
}
