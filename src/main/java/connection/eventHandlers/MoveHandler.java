package connection.eventHandlers;

import connection.Connection;
import framework.player.BlockingPlayer;
import connection.Parser;
import framework.*;

import java.text.ParseException;
import java.util.HashMap;

public class MoveHandler extends EventHandler {
    private BlockingPlayer player;
    public MoveHandler(Connection connection) {
        super(connection);
    }

    public synchronized void setPlayer(BlockingPlayer player) {
        this.player = player;
    }

    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game", "move"}, message);
    }

    public void handle(String[] message) {
        try {
            String rawDetails = Parser.sliceStringFromParts(message, 3, message.length);
            HashMap<String, String> details = Parser.parseMap(rawDetails);
            if (details.get("PLAYER").equals(player.getUsername())) {
                int boardSize = connection.getFramework().getBoardSize();
                int rawCell = Integer.parseInt(details.get("MOVE"));
                Move move = new Move(GameState.RemoteTurn, rawCell % boardSize, rawCell / boardSize);
                player.putMove(move);
            }
        } catch (ParseException e) {
            System.err.println(e.toString());
        }

    }
}
