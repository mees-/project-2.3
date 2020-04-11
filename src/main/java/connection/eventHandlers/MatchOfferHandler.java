package connection.eventHandlers;

import connection.Connection;
import framework.player.BlockingPlayer;
import connection.Parser;
import framework.GameState;
import framework.GameType;
import framework.player.Player;
import framework.player.RemotePlayer;

import java.text.ParseException;
import java.util.HashMap;

public class MatchOfferHandler extends EventHandler {
    public MatchOfferHandler(Connection connection) {
        super(connection);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game", "match"}, message);
    }

    @Override
    public EventPayload handle(String[] message) throws ParseException {
        String rawMap = Parser.sliceStringFromParts(message, 3, message.length);
        HashMap<String, String> details = Parser.parseMap(rawMap);
        GameType gameType = GameType.fromString(details.get("GAMETYPE"));
        BlockingPlayer remotePlayer = new RemotePlayer(details.get("OPPONENT"), gameType);
        connection.setRemotePlayer(remotePlayer);
        GameState startingState;
        if (details.get("OPPONENT").equals(details.get("PLAYERTOMOVE"))) {
            startingState = GameState.TurnTwo;
        } else {
            startingState = GameState.TurnOne;
        }
        return new MatchOffer(gameType, remotePlayer, startingState);
    }

    public static class MatchOffer extends EventPayload {
        private GameType gameType;
        private Player remotePlayer;
        private GameState startingState;
        MatchOffer(GameType type, Player remotePlayer, GameState startingState) {
            super(EventType.MatchOffer);
            this.gameType = type;
            this.remotePlayer = remotePlayer;
            this.startingState = startingState;
        }

        public GameType getGameType() {
            return gameType;
        }

        public Player getRemotePlayer() {
            return remotePlayer;
        }

        public GameState getStartingState() {
            return startingState;
        }
    }
}
