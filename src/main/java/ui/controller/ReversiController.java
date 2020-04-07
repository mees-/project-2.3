package ui.controller;

import framework.Framework;
import framework.player.Player;
import ui.Main;

public class ReversiController {
    Framework framework;
    Main main;
    Player localPlayer;

    public ReversiController(Main main, Framework framework) {
        this.framework = framework;
        this.main = main;
        localPlayer = this.framework.getLocalPlayer();

//        tile-reversi-available
    }


}
