package connection.eventHandlers;

import framework.Framework;
import framework.MoveResult;

public class GameEndHandler extends EventHandler {
    public GameEndHandler(Framework framework) {
        super(framework);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"svr", "game"}, message)
                && MoveResult.fromString(message[2]) != null;
    }

    @Override
    public void handle(String[] message) {
        MoveResult result = MoveResult.fromString(message[2]);
        framework.notifyState(result);
        if (result == MoveResult.Win) {
            System.out.println("You win!");
        }
    }
}
