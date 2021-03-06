package framework;

import ai.Ai;
import connection.Connection;
import connection.commands.ChallengeAcceptCommand;
import connection.commands.ChallengeCommand;
import connection.eventHandlers.ChallengeHandler;
import connection.commands.GetPlayerListCommand;
import connection.commands.LogoutCommand;
import connection.commands.response.PlayerList;
import connection.eventHandlers.EventPayload;
import connection.eventHandlers.MatchOfferHandler;
import framework.player.ChallengePlayer;
import framework.player.ComposablePlayer;
import framework.player.Player;
import framework.player.RemotePlayer;
import reversi.ReversiGame;
import tictactoe.TicTacToeGame;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Framework {
    private Match match;
    private final Player localPlayer;
    private final Connection connection;
    private final Thread eventLoopThread = new Thread(this::eventLoop);
    private BlockingQueue<Match> matchQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<ChallengePlayer> challengeQueue = new LinkedBlockingQueue<>();
    private final ArrayList<ChallengePlayer> openChallenges = new ArrayList<>();

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
                if (((ComposablePlayer)localPlayer).getSource() instanceof Ai) {
                    ((Ai) ((ComposablePlayer)localPlayer).getSource()).reset();
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
            case Challenge: {
                ChallengeHandler.ChallengePayload challengeOffer = (ChallengeHandler.ChallengePayload) payload;
                String name = challengeOffer.getChallenger();
                GameType game = challengeOffer.getGame();
                Integer challengeNumber = challengeOffer.getChallengeNumber();

                try {
                    challengeQueue.put(new ChallengePlayer(name, game, challengeNumber));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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

    public List<String> getPlayers() {
        try {
            return connection.executeCommand(new GetPlayerListCommand()).get().getPlayers();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChallenge(String name, GameType gameType) {
        connection.executeCommand(new ChallengeCommand(name, gameType));
    }

    public void acceptChallenge(Integer challengeNumber) {
        connection.executeCommand(new ChallengeAcceptCommand(challengeNumber));
    }

    public void retrieveChallenges() {
        synchronized (openChallenges) {
            try {
                ChallengePlayer player = challengeQueue.take();
                openChallenges.add(player);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<ChallengePlayer> getChalllenges() {
        return openChallenges;
    }
}