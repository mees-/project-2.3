package connection.eventHandlers;

import connection.Connection;
import framework.player.BlockingPlayer;
import connection.Parser;
import framework.*;

import java.text.ParseException;
import java.util.HashMap;

public class MoveHandler extends EventHandler {
    public MoveHandler(Connection connection) {
        super(connection);
    }


    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game", "move"}, message);
    }

    public EventPayload handle(String[] message) {
        try {
            String rawDetails = Parser.sliceStringFromParts(message, 3, message.length);
            HashMap<String, String> details = Parser.parseMap(rawDetails);
            if (details.get("PLAYER").equals(connection.getRemotePlayer().getUsername())) {
                int boardSize = connection.getFramework().getBoardSize();
                int rawCell = Integer.parseInt(details.get("MOVE"));
                Move move = new Move(GameState.TurnTwo, rawCell % boardSize, rawCell / boardSize);
                connection.getRemotePlayer().putMove(move);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
