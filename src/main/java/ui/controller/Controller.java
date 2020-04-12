package ui.controller;

import framework.Match;
import framework.player.Player;
import framework.player.Players;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import ui.Main;

public abstract class Controller {
    protected Main main;
    protected Player currentPlayer;
    protected Match match;

    @FXML
    protected Text txtPlayerOne;

    @FXML
    protected Text txtPlayerTwo;

    @FXML
    protected SVGPath wcPlayerOne;

    @FXML
    protected SVGPath wcPlayerTwo;

    @FXML
    protected SVGPath tPlayerOne;

    @FXML
    protected SVGPath tPlayerTwo;

    @FXML
    protected Text sPlayerOne;

    @FXML
    protected Text sPlayerTwo;

    protected ObservableList<Node> childNodes;

    protected Players players;

    protected Thread run = new Thread(this::run);

    public Controller(Main main, Match match) {
        this.main = main;
        this.match = match;
    }

    abstract protected void run();
}
