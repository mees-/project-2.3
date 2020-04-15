package framework.player;

import framework.GameType;

public class ChallengePlayer extends RemotePlayer {
    private Integer challengeNumber;

    public ChallengePlayer(String username, GameType gameType, Integer challengeNumber) {
        super(username, gameType);
        this.challengeNumber = challengeNumber;
    }

    public Integer getChallengeNumber() {
        return challengeNumber;
    }
}
