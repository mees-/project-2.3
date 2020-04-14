package connection.commands;

import connection.commands.response.StandardResponse;

public class LoginCommand extends Command<StandardResponse> {
    private final String username;

    public LoginCommand(String username) {
        this.username = username;
    }

    @Override
    public String getCommandString() {
        return "LOGIN " + username;
    }

    @Override
    public boolean isValidResponse(String[] response) {
        return StandardResponse.isStandardResponse(response);
    }

    @Override
    public StandardResponse parseResponse(String[] response) {
        return StandardResponse.isOk(response);
    }
}
