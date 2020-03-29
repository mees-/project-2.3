package connection.eventHandlers;

import connection.Parser;
import framework.Framework;
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
            framework.notifyGameOffer(
                    GameType.fromString(details.get("GAMETYPE")),
                    details.get("OPPONENT")
                    );
        } catch (ParseException e) {
            System.err.println(e.toString());
        }
    }
}
