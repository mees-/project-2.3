package connection.eventHandlers;

import connection.Parser;
import framework.Framework;
import framework.Move;
import framework.PlayerType;

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
            Move move = new Move(PlayerType.Remote, 0, 0);
//            move.player = PlayerType.Remote;
            // TODO: add more to move
            framework.notifyMove(move);
        } catch (ParseException e) {
            System.err.println(e.toString());
        }

    }
}
