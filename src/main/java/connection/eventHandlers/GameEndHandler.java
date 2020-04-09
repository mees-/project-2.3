package connection.eventHandlers;

import connection.Connection;
import connection.Parser;
import framework.ForfeitMove;
import framework.GameState;

import java.text.ParseException;
import java.util.HashMap;

public class GameEndHandler extends EventHandler {
    public GameEndHandler(Connection connection) {
        super(connection);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game"}, message)
                && GameState.fromString(message[2]) != null;
    }

    @Override
    public EventPayload handle(String[] message) {
        if (message[2].equalsIgnoreCase("win")) {
            try {
                HashMap<String, String> details = Parser.parseMap(
                        Parser.sliceStringFromParts(message, 3, message.length)
                );
                if (
                        details.get("COMMENT").equalsIgnoreCase("Player forfeited match")
                        || details.get("COMMENT").equalsIgnoreCase("Client disconnected")
                ) {
                    connection.getRemotePlayer().putMove(new ForfeitMove(connection.getRemotePlayer().getTurn()));
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
