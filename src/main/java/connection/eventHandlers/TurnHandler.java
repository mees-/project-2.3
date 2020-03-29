package connection.eventHandlers;

import framework.Framework;
import framework.GameState;

public class TurnHandler extends EventHandler {
    public TurnHandler(Framework framework) {
        super(framework);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game", "yourturn"}, message);
    }

    @Override
    public void handle(String[] message) {
        framework.notifyState(GameState.LocalTurn);
    }
}
