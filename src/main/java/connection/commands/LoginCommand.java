package connection.commands;

public class LoginCommand implements ICommand {
    private final String username;

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
