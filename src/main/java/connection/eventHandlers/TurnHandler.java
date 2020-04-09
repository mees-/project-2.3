package connection.eventHandlers;

import connection.Connection;

public class TurnHandler extends EventHandler {
    public TurnHandler(Connection connection) {
        super(connection);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game", "yourturn"}, message);
    }

    @Override
    public EventPayload handle(String[] message) {
//        framework.notifyState(GameState.LocalTurn);
        return null;
    }
}
