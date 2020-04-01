package connection.commands;

import framework.GameType;

public class SubscribeCommand implements ICommand {
    private final GameType gameType;

    public SubscribeCommand(GameType gameType) {
        this.gameType = gameType;
    }

    public String getCommandString() {
        return "SUBSCRIBE " + gameType.getToken();
    }

    public boolean isValidResponse(String[] response) {
        return ICommand.isStandardResponse(response);
    }

    public CommandResponse parseResponse(String[] response) {
        return ICommand.isOk(response);
    }
}
