package ui.controller;

import ai.Ai;
import framework.BoardInterface;
import framework.GameState;
import framework.Match;
import framework.player.Players;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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

    public void setup(Match match) {
        this.match = match;
    }

    public void start() {
        run.start();
    }

    abstract protected void run();

    abstract protected void updateBoard(BoardInterface board, GameState gameState);

    abstract protected void setupNames();

    protected void setTurns(GameState gameState) {
        if (!gameState.isEnd()) {
            if (match.getGameState() == GameState.TurnOne) {
                tPlayerOne.setVisible(true);
                tPlayerTwo.setVisible(false);
            } else {
                tPlayerTwo.setVisible(true);
                tPlayerOne.setVisible(false);
            }
        } else {
            tPlayerOne.setVisible(false);
            tPlayerTwo.setVisible(false);
        }
    }

    protected void isEnd(GameState gameState) {
        switch (gameState) {
            case OneWin:
                wcPlayerOne.setVisible(true);
                break;
            case TwoWin:
                wcPlayerTwo.setVisible(true);
                break;
            case Draw:
                wcPlayerOne.setVisible(true);
                wcPlayerTwo.setVisible(true);
        }
        Platform.runLater(this::resetMatch);
    }

    protected void resetMatch() {
        Player sourceOne = match.getPlayers().one.getSource();
        Player sourceTwo = match.getPlayers().two.getSource();
        if (sourceOne instanceof Ai) {
            ((Ai) sourceOne).reset();
        }
        if (sourceTwo instanceof Ai) {
            ((Ai) sourceTwo).reset();
        }

        match = null;
    }

    abstract protected void mouseClick(MouseEvent event);

    @FXML
    private void forfeit() {
        match.getCurrentPlayer().forfeit();
        main.changePane();
    }
}
