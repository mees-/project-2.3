package framework.player;

import framework.GameType;

public class LocalPlayer extends BlockingPlayer {
    public LocalPlayer(String username, GameType gameType) {
        super(username, gameType);
    }
}
