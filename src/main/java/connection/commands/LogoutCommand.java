package connection.commands;

import connection.commands.response.StandardResponse;

public class LogoutCommand extends Command<StandardResponse> {
    @Override
    public String getCommandString() {
        return "logout";
    }

    @Override
    public boolean isValidResponse(String[] response) {
        // this command expects no response so always return false
        return false;
    }

    @Override
    public StandardResponse parseResponse(String[] response) {
        return null;
    }
}