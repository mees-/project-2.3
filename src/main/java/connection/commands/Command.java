package connection.commands;

import connection.GenericFuture;
import connection.commands.response.StandardResponse;

public abstract class Command<R extends StandardResponse> {
    protected GenericFuture<R> future = new GenericFuture<>();

    public abstract String getCommandString();

    public abstract boolean isValidResponse(String[] response);

    public abstract R parseResponse(String[] response);

    public R parseAndHandleResponse(String[] message) {
        R response = parseResponse(message);
        future.resolve(response);
        return response;
    }

    public GenericFuture<R> getFuture() {
        return future;
    }


}
