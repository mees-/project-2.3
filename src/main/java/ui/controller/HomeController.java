package ui.controller;

import framework.GameType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ui.Main;
import ui.settings.*;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button btnSubscribe;

    @FXML
    private Button btnChallenge;

    @FXML
    private Button btnOnline;

    @FXML
    private Button btnLocal;

    @FXML
    private VBox vbPlayerTwo;

    @FXML
    private VBox vbOnlineOption;

    @FXML
    private ToggleGroup tgPlayerOne;

    @FXML
    private ToggleGroup tgPlayerTwo;

    @FXML
    private RadioButton rbPlayerOneHuman;

    @FXML
    private RadioButton rbPlayerOneAI;

    @FXML
    private RadioButton rbPlayerTwoHuman;

    @FXML
    private RadioButton rbPlayerTwoAI;

    @FXML
    private VBox vbTicTacToe;

    @FXML
    private VBox vbReversi;

    @FXML
    private TextField txtPlayerOneName;

    @FXML
    private TextField txtPlayerTwoName;

    private PlayType playTypeEnum;
    private OnlineOption onlineOptionEnum;
    private PlayerType playerOneTypeEnum;
    private PlayerType playerTwoTypeEnum;
    private GameType chosenGameEnum;
    private Main main;

    public HomeController(Main main, PlayType gameTypeEnum, OnlineOption onlineOptionEnum, PlayerType playerOneTypeEnum, PlayerType playerTwoTypeEnum, GameType chosenGameEnum) {
        this.main = main;
        this.playTypeEnum = gameTypeEnum;
        this.onlineOptionEnum = onlineOptionEnum;
        this.playerOneTypeEnum = playerOneTypeEnum;
        this.playerTwoTypeEnum = playerTwoTypeEnum;
        this.chosenGameEnum = chosenGameEnum;
    }

    @FXML
    private void optionButtonEvent(ActionEvent event) {
        Button btnSource = (Button) event.getSource();
        Button btnOne = btnChallenge, btnTwo = btnSubscribe;
        onlineOptionEnum = OnlineOption.CHALLENGE;

        if (btnSource.getId().equals("btnSubscribe")) {
            btnOne = btnSubscribe;
            btnTwo = btnChallenge;
            onlineOptionEnum = OnlineOption.SUBSCRIBE;
        }

        btnChange(btnOne, btnTwo);
    }

    @FXML
    private void typeButtonEvent(ActionEvent event) {
        Button btnSource = (Button) event.getSource();
        Button btnOne = btnLocal, btnTwo = btnOnline;

        if (btnSource.getId().equals("btnOnline")) {
            btnOne = btnOnline;
            btnTwo = btnLocal;
            playTypeEnum = PlayType.ONLINE;
            vbPlayerTwo.setVisible(false);
            vbOnlineOption.setVisible(true);
        } else {
            playTypeEnum = PlayType.LOCAL;
            vbPlayerTwo.setVisible(true);
            vbOnlineOption.setVisible(false);
        }

        btnChange(btnOne, btnTwo);
    }

    private void btnChange(Button btnOne, Button btnTwo) {
        btnOne.getStyleClass().add("btn-primary");
        btnTwo.getStyleClass().remove("btn-primary");
    }

    @FXML
    private void playerTypeEvent(ActionEvent event) {
        RadioButton rbPlayerType = (RadioButton) event.getSource();

        if (rbPlayerType.equals(rbPlayerOneHuman) || rbPlayerType.equals(rbPlayerOneAI)) {
            playerOneTypeEnum = rbChange(tgPlayerOne);
        } else if (rbPlayerType.equals(rbPlayerTwoHuman) || rbPlayerType.equals(rbPlayerTwoAI)) {
            playerTwoTypeEnum = rbChange(tgPlayerTwo);
        }
    }

    private PlayerType rbChange(ToggleGroup toggleGroup) {
        RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (radioButton.getText().equals("AI")) {
            return PlayerType.AI;
        } else if (radioButton.getText().equals("Human")) {
            return PlayerType.HUMAN;
        }

        return null;
    }

    @FXML
    private void chosenGameEvent(MouseEvent event) {
        VBox vbSource = (VBox) event.getSource();
        VBox vbOne = vbReversi, vbTwo = vbTicTacToe;
        chosenGameEnum = GameType.Reversi;

        if (vbSource.equals(vbTicTacToe)) {
            vbOne = vbTicTacToe;
            vbTwo = vbReversi;
            chosenGameEnum = GameType.TicTacToe;
        }

        gameChange(vbOne, vbTwo);
    }

    private void gameChange(VBox vbOne, VBox vbTwo) {
        if (!vbOne.getStyleClass().contains("panel-game-active")) {
            vbOne.getStyleClass().add("panel-game-active");
        }
        vbTwo.getStyleClass().remove("panel-game-active");
    }

    @FXML
    private void playButtonEvent(ActionEvent event) throws IOException {
        if (chosenGameEnum != null && playTypeEnum != null ) {
            if (playTypeEnum == PlayType.LOCAL) {
                if (playerOneTypeEnum != null && playerTwoTypeEnum != null) {
                    main.changePane(chosenGameEnum, txtPlayerOneName.getText(), txtPlayerTwoName.getText(), playerOneTypeEnum, playerTwoTypeEnum);
                }
            } else if (onlineOptionEnum != null) {
                Button playButton = (Button) event.getSource();

                main.changePane(chosenGameEnum, txtPlayerOneName.getText(), playerOneTypeEnum);
            }
        }
    }
}
