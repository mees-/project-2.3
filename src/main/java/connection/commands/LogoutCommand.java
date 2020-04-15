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
        return response == null;
    }

    @Override
    public StandardResponse parseResponse(String[] response) {
        if (response == null) {
            return new StandardResponse(true);
        } else {
            throw new RuntimeException("Shouldn't be here");
        }
    }

    @Override
    public StandardResponse parseResponse(String[][] response) {
        if (response != null) {
            throw new RuntimeException("Invalid response");
        } else {
            return new StandardResponse(true);
        }
    }
}
