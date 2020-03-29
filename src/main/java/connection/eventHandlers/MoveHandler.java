package connection.eventHandlers;

import connection.Parser;
import framework.*;

import java.text.ParseException;
import java.util.HashMap;

public class MoveHandler extends EventHandler {
    public MoveHandler(Framework framework) {
        super(framework);
    }

    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game", "move"}, message);
    }

    public void handle(String[] message) {
        try {
            String rawDetails = Parser.sliceStringFromParts(message, 3, message.length);
            HashMap<String, String> details = Parser.parseMap(rawDetails);
            if (!details.get("PLAYER").equals(framework.getState().getLocalUsername())) {
                int boardSize = framework.getState().getBoard().getSize();
                int rawCell = Integer.parseInt(details.get("MOVE"));
                Move move = new Move(MoveResult.RemoteTurn, rawCell % boardSize, rawCell / boardSize);
                try {
                    framework.move(move);
                } catch (InvalidMoveException e) {
                    System.err.println("Remote did an invalid move!!!!");
                }

            }
        } catch (ParseException e) {
            System.err.println(e.toString());
        }

    }
}
