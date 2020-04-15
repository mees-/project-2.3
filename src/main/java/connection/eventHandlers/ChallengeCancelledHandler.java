package connection.eventHandlers;

import connection.Connection;
import connection.Parser;
import framework.GameType;

import java.text.ParseException;
import java.util.HashMap;

public class ChallengeCancelledHandler extends EventHandler<ChallengeCancelledHandler.ChallengeCancelledPayload> {
    public ChallengeCancelledHandler(Connection connection) {
        super(connection);
    }

    @Override
    public boolean isValidMessage(String[] message) {
        return EventHandler.validateWords(new String[]{"SVR", "GAME", "CHALLENGE", "CANCELLED"}, message);
    }

    public ChallengeCancelledPayload handle(String[] message) throws ParseException {
        HashMap<String, String> details = Parser.parseMap(Parser.sliceStringFromParts(message, 3, message.length));
        int challengeNumber = Integer.parseInt(details.get("CHALLENGENUMBER"));
        return new ChallengeCancelledPayload(challengeNumber);
    }

    public static class ChallengeCancelledPayload extends EventPayload {

        private int challengeNumber;

        ChallengeCancelledPayload(int challengeNumber) {
            super(EventType.ChallengeCancelled);
            this.challengeNumber = challengeNumber;
        }

        public int getChallengeNumber() {
            return challengeNumber;
        }
    }
}