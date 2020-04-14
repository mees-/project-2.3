package connection.commands;

import connection.commands.response.StandardResponse;

public class ForfeitCommand extends Command<StandardResponse> {
    @Override
    public String getCommandString() {
        return "forfeit";
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
