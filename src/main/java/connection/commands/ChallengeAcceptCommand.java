package connection.commands;

import connection.commands.response.StandardResponse;

public class ChallengeAcceptCommand extends Command<StandardResponse> {

    private Integer challengeNumber = null;

    public ChallengeAcceptCommand(Integer challengeNumber) {
        this.challengeNumber = challengeNumber;
    }

    @Override
    public String getCommandString() {
        return "challenge accept " + challengeNumber.toString();
    }

    @Override
    public boolean isValidResponse(String[] response) {
        return StandardResponse.isStandardResponse(response);
    }

    @Override
    public StandardResponse parseResponse(String[] response) {
        return new StandardResponse(response);
    }
}
