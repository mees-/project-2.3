package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.settings.GameType;
import ui.settings.OnlineOption;
import ui.settings.PlayerType;

public class HomeController {
    @FXML
    public ChoiceBox cbPlayerTwoType;

    @FXML
    private ChoiceBox cbPlayerOneType;

    @FXML
    private Button btnSubscribe;

    @FXML
    private Button btnChallenge;

    @FXML
    private Button btnOnline;

    @FXML
    private Button btnLocal;

    @FXML
    private HBox hbPlayerTwo;

    @FXML
    private VBox vbOnlineOption;

    private GameType gameTypeEnum;
    private OnlineOption onlineOptionEnum;
    private PlayerType playerOneTypeEnum;
    private PlayerType playerTwoTypeEnum;

    public HomeController() {
        gameTypeEnum = GameType.LOCAL;
        onlineOptionEnum = OnlineOption.SUBSCRIBE;
        playerOneTypeEnum = PlayerType.HUMAN;
        playerTwoTypeEnum = PlayerType.HUMAN;
    }

    public void optionButtonEvent(ActionEvent event) {
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

    public void typeButtonEvent(ActionEvent event) {
        Button btnSource = (Button) event.getSource();
        Button btnOne = btnLocal, btnTwo = btnOnline;

        if (btnSource.getId().equals("btnOnline")) {
            btnOne = btnOnline;
            btnTwo = btnLocal;
            gameTypeEnum = GameType.ONLINE;
            hbPlayerTwo.setVisible(true);
            vbOnlineOption.setVisible(true);
        } else {
            gameTypeEnum = GameType.LOCAL;
            hbPlayerTwo.setVisible(false);
            vbOnlineOption.setVisible(false);
        }

        btnChange(btnOne, btnTwo);
    }

    private void btnChange(Button btnOne, Button btnTwo) {
        if (btnOne.getStyleClass().contains("btn-sm")) {
            btnOne.getStyleClass().add("btn-lg");
            btnOne.getStyleClass().remove("btn-sm");
        }
        if (btnTwo.getStyleClass().contains("btn-lg")) {
            btnTwo.getStyleClass().remove("btn-lg");
            btnTwo.getStyleClass().add("btn-sm");
        }
    }

    public void playerTypeEvent(ActionEvent event) {
        ChoiceBox cbPlayerType = (ChoiceBox) event.getSource();

        if (cbPlayerType.getId().equals("cbPlayerOneType")) {
            playerOneTypeEnum = cbChange(cbPlayerType);
        } else if (cbPlayerType.getId().equals("cbPlayerTwoType")) {
            playerTwoTypeEnum = cbChange(cbPlayerType);
        }

        System.out.println(hbPlayerTwo);

    }

    private PlayerType cbChange(ChoiceBox choiceBox) {
        if (choiceBox.valueProperty().getValue().equals("AI")) {
            return PlayerType.AI;
        } else if (choiceBox.valueProperty().getValue().equals("HUMAN")) {
            return PlayerType.HUMAN;
        }

        return null;
    }
}
