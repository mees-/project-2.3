package connection.eventHandlers;

import connection.Connection;
import framework.player.BlockingPlayer;
import connection.Parser;
import framework.GameState;
import framework.GameType;

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
    public void handle(String[] message) {
        String rawMap = Parser.sliceStringFromParts(message, 3, message.length);
        try {
            HashMap<String, String> details = Parser.parseMap(rawMap);
            BlockingPlayer remotePlayer = new BlockingPlayer(details.get("OPPONENT"));
            connection.setPlayer(remotePlayer);
            GameState startingState;
            if (details.get("OPPONENT").equals(details.get("PLAYERTOMOVE"))) {
                startingState = GameState.RemoteTurn;
            } else {
                startingState = GameState.LocalTurn;
            }
            connection.getFramework().notifyGameOffer(
                    GameType.fromString(details.get("GAMETYPE")),
                    remotePlayer,
                    startingState
                    );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
