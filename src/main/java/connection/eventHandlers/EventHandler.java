package connection.eventHandlers;

import connection.Connection;

public abstract class EventHandler {
    protected final Connection connection;

    public EventHandler(Connection connection) {
        this.connection = connection;
    }

    public abstract boolean isValidMessage(String[] message);

    public abstract void handle(String[] message);

    public static boolean validateWords(String[] wanted, String[] input) {
        if (wanted.length > input.length) {
            return false;
        }
        for (int i = 0; i < wanted.length; i++) {
            if (!wanted[i].equalsIgnoreCase(input[i])) {
                return false;
            }
        }
        return true;
    }
}
