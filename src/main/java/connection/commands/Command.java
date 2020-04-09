package connection.commands;

import connection.commands.response.StandardResponse;

import java.util.Arrays;

public abstract class Command<R extends StandardResponse> {
    protected CommandFuture<R> future = new CommandFuture<R>();

    public abstract String getCommandString();

    public abstract boolean isValidResponse(String[] response);

    public abstract R parseResponse(String[] response);

    public R parseAndHandleResponse(String[] message) {
        R response = parseResponse(message);
        future.setResponse(response);
        return response;
    }

    public CommandFuture<R> getFuture() {
        return future;
    }


}
