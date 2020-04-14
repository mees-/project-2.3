package connection.commands;

import connection.commands.response.StandardResponse;
import framework.GameType;

public class SubscribeCommand extends Command<StandardResponse> {
    private final GameType gameType;

    public SubscribeCommand(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public String getCommandString() {
        return "SUBSCRIBE " + gameType.toString();
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
