package connection.eventHandlers;

import connection.Parser;
import framework.Framework;
import framework.GameState;
import framework.GameType;

import java.text.ParseException;
import java.util.HashMap;

public class MatchOfferHandler extends EventHandler {
    public MatchOfferHandler(Framework framework) {
        super(framework);
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
            GameState startingPlayer;
            if (details.get("OPPONENT").equals(details.get("PLAYERTOMOVE"))) {
                startingPlayer = GameState.RemoteTurn;
            } else {
                startingPlayer = GameState.LocalTurn;
            }
            framework.notifyGameOffer(
                    GameType.fromString(details.get("GAMETYPE")),
                    details.get("OPPONENT"),
                    startingPlayer
                    );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
