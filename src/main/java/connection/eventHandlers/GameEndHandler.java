package connection.eventHandlers;

import framework.Framework;
import framework.GameState;

public class GameEndHandler extends EventHandler {
    public GameEndHandler(Framework framework) {
        super(framework);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game"}, message)
                && GameState.fromString(message[2]) != null;
    }

    @Override
    public void handle(String[] message) {
        GameState result = GameState.fromString(message[2]);
        framework.notifyState(result);
        if (result == GameState.Win) {
            System.out.println("You win!");
        }
    }
}
