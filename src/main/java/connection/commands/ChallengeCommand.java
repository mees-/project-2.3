package connection.commands;

import connection.commands.response.StandardResponse;
import framework.GameType;

public class ChallengeCommand extends Command<StandardResponse> {
    private String player;
    private GameType game;

    public ChallengeCommand(String player, GameType game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public String getCommandString() {
        return "challenge \"" + player + "\" \"" + game.toString() + "\"";
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
