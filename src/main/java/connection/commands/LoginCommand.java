package connection.commands;

import java.util.Arrays;

public class LoginCommand implements ICommand {
    private String username;

    public LoginCommand(String username) {
        this.username = username;
    }

    public String getCommandString() {
        return "LOGIN " + username;
    }

    public boolean isValidResponse(String[] response) {
        return ICommand.isStandardResponse(response);
    }

    public CommandResponse parseResponse(String[] response) {
        return ICommand.isOk(response);
    }
}
