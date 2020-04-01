package connection.eventHandlers;

import connection.Connection;
import framework.GameState;

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
    public void handle(String[] message) {
//        GameState result = GameState.fromString(message[2]);
    }
}
