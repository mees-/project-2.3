package framework;

import connection.Connection;
import connection.GenericFuture;
import connection.commands.LogoutCommand;
import connection.eventHandlers.EventPayload;
import connection.eventHandlers.MatchOfferHandler;
import framework.player.Player;
import reversi.ReversiGame;
import tictactoe.TicTacToeGame;

import java.io.IOException;

public class Framework {
    private Match match;
    private final Player localPlayer;
    private final Connection connection;
    private final Thread eventLoopThread = new Thread(this::eventLoop);
    private GenericFuture<Match> matchFuture = new GenericFuture<>();

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
                break;
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
                        game = new TicTacToeGame();
                        break;
                    case Reversi:
                        game = new ReversiGame();
                        break;
                }
                match = new Match(game, localPlayer, matchOffer.getRemotePlayer());
                synchronized (match) {
                    match.setupGame(matchOffer.getStartingState());
                    match.startAsync();
                    matchFuture.resolve(match);
                }
                break;
            }
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

    public synchronized GenericFuture<Match> getMatchFuture() {
        return matchFuture;
    }
}
