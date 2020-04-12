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

public abstract class GameController {
    protected Main main;
    protected Match match;

    @FXML
    protected GridPane gpGame;

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

    public GameController(Main main) {
        this.main = main;
    }

    abstract protected void run();

    public void setup(Match match) {
        this.match = match;
    }

    abstract public void start();
}
