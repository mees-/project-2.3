package ui.controller;

import framework.Framework;
import framework.GameInterface;
import framework.GameType;
import framework.Match;
import javafx.fxml.FXML;
import ui.Main;
import ui.settings.PlayerType;

import java.io.IOException;

public class LobbyController {
    private String playerName;
    private PlayerType playerType;
    private Framework framework;
    private Match match;
    private Main main;
    private Thread tournamentThread = new Thread(() -> {

        if (!main.isTournament()) {
            try {
                match = framework.getMatchFuture().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            main.setTournament(true);
            main.onlineGameSetup(match.getGame().getType(), playerName, playerType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    });


    public LobbyController(Main main, Framework framework, String playerName, PlayerType playerType) {
        this.framework = framework;
        this.main = main;
        this.playerName = playerName;
        this.playerType = playerType;
    }

    public void tournamentSetup() {
        tournamentThread.start();
    }
}
