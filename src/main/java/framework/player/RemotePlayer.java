package framework.player;

import framework.GameType;

public class RemotePlayer extends BlockingPlayer {

    public RemotePlayer(String username, GameType gameType) {
        super(username, gameType);
    }
}
