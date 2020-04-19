package ui.controller;

import framework.Framework;
import framework.GameType;
import framework.Match;
import ui.Main;
import ui.settings.PlayerType;

import java.io.IOException;

public class LobbyController {
    private String playerName;
    private PlayerType playerType;
    private Framework framework;
    private Match match;
    private Main main;
    private GameType gameType;

    private Thread tournamentThread = new Thread(() -> {
        while (true) {
            try {
                match = framework.getNextMatch();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                main.gameSetupLobby(match.getGame().getType(), match);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    public LobbyController(Main main, Framework framework, String playerName, PlayerType playerType, GameType gameType) {
        this.framework = framework;
        this.main = main;
        this.playerName = playerName;
        this.playerType = playerType;
        this.gameType = gameType;
    }

    public void tournamentSetup() {
        tournamentThread.start();
    }
}
