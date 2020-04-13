package framework;

import connection.Connection;
import connection.GenericFuture;
import connection.commands.ChallengeCommand;
import connection.commands.GetPlayerListCommand;
import connection.commands.LogoutCommand;
import connection.commands.response.PlayerList;
import connection.commands.response.StandardResponse;
import connection.eventHandlers.EventPayload;
import connection.eventHandlers.MatchOfferHandler;
import framework.player.Player;
import reversi.ReversiGame;
import tictactoe.TicTacToeGame;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Framework {
    private Match match;
    private final Player localPlayer;
    private final Connection connection;
    private final Thread eventLoopThread = new Thread(this::eventLoop);
    private BlockingQueue<Match> matchQueue = new LinkedBlockingQueue<>();

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
                    try {
                        matchQueue.put(match);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
        }
    }

    public void close() throws IOException, InterruptedException {
        connection.executeCommand(new LogoutCommand()).waitForResolve();
        connection.close();
        eventLoopThread.interrupt();
    }

    public Match getMatch() {
        return match;
    }

    public Match getNextMatch() throws InterruptedException {
        return matchQueue.take();
    }

    public PlayerList getPlayers() {
        try {
            return (PlayerList) connection.executeCommand(new GetPlayerListCommand()).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChallenge(String name, GameType gameType) {
        connection.executeCommand(new ChallengeCommand("", gameType));
    }

    public void cancelChallenge() {
//        connection.executeCommand()
    }
}