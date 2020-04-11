package connection.eventHandlers;

import connection.Connection;
import connection.Parser;
import framework.GameType;

import java.text.ParseException;
import java.util.HashMap;

public class ChallangeHandler extends EventHandler<ChallangeHandler.ChallengePayload> {
    public ChallangeHandler(Connection connection) {
        super(connection);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"SVR", "GAME", "CHALLENGE"}, message);
    }

    @Override
    public ChallengePayload handle(String[] message) throws ParseException {
        HashMap<String, String> details = Parser.parseMap(Parser.sliceStringFromParts(message, 3, message.length));
        String challengeUser = details.get("CHALLENGER");
        GameType gameType = GameType.fromString(details.get("GAMETYPE"));
        int challengeNumber = Integer.parseInt(details.get("CHALLENGENUMBER"));
        return new ChallengePayload(challengeUser, gameType, challengeNumber);
    }

    public static class ChallengePayload extends EventPayload {

        private String challenger;
        private GameType game;
        private int challengeNumber;

        ChallengePayload(String challenger, GameType game, int challengeNumber) {
            super(EventType.Challenge);
            this.challenger = challenger;
            this.game = game;
            this.challengeNumber = challengeNumber;
        }

        public String getChallenger() {
            return challenger;
        }

        public GameType getGame() {
            return game;
        }

        public int getChallengeNumber() {
            return challengeNumber;
        }
    }
}
