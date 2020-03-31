package connection.eventHandlers;

import connection.ConnectionPlayer;
import connection.Parser;
import framework.*;

import java.text.ParseException;
import java.util.HashMap;

public class MoveHandler extends EventHandler {
    private ConnectionPlayer player;
    public MoveHandler(Framework framework, ConnectionPlayer player) {
        super(framework);
        this.player = player;
    }

    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game", "move"}, message);
    }

    public void handle(String[] message) {
        try {
            String rawDetails = Parser.sliceStringFromParts(message, 3, message.length);
            HashMap<String, String> details = Parser.parseMap(rawDetails);
            if (!details.get("PLAYER").equals(framework.getState().getLocalUsername())) {
                int boardSize = framework.getBoardSize();
                int rawCell = Integer.parseInt(details.get("MOVE"));
                Move move = new Move(GameState.RemoteTurn, rawCell % boardSize, rawCell / boardSize);
                player.putMove(move);
            }
        } catch (ParseException e) {
            System.err.println(e.toString());
        }

    }
}
