package ui.controller;

import framework.Framework;
import framework.GameType;
import framework.Match;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import ui.Main;
import ui.settings.ButtonType;
import ui.settings.PlayerType;

import java.io.IOException;

public class LobbyController {
    @FXML
    private VBox playersTable;

    @FXML
    private VBox challengesTable;

    private static final String SVGcancel = "M 8 0.25 C 3.71875 0.25 0.25 3.71875 0.25 8 C 0.25 12.28125 3.71875 15.75 8 15.75 C 12.28125 15.75 15.75 12.28125 15.75 8 C 15.75 3.71875 12.28125 0.25 8 0.25 Z M 12.066406 3.933594 C 14.109375 5.980469 14.253906 9.105469 12.710938 11.296875 L 4.703125 3.289062 C 6.894531 1.746094 10.023438 1.890625 12.066406 3.933594 Z M 3.933594 12.066406 C 1.890625 10.019531 1.746094 6.894531 3.289062 4.703125 L 11.296875 12.710938 C 9.105469 14.253906 5.976562 14.109375 3.933594 12.066406 Z M 3.933594 12.066406";
    private static final String SVGchallenge = "M 18.5 6 L 14.789062 6 C 15.1875 6.925781 15.011719 8.039062 14.257812 8.792969 L 10 13.050781 L 10 14.5 C 10 15.328125 10.671875 16 11.5 16 L 18.5 16 C 19.328125 16 20 15.328125 20 14.5 L 20 7.5 C 20 6.671875 19.328125 6 18.5 6 Z M 15 11.75 C 14.585938 11.75 14.25 11.414062 14.25 11 C 14.25 10.585938 14.585938 10.25 15 10.25 C 15.414062 10.25 15.75 10.585938 15.75 11 C 15.75 11.414062 15.414062 11.75 15 11.75 Z M 13.550781 5.914062 L 8.085938 0.449219 C 7.484375 -0.148438 6.515625 -0.148438 5.914062 0.449219 L 0.449219 5.914062 C -0.148438 6.515625 -0.148438 7.484375 0.449219 8.085938 L 5.914062 13.550781 C 6.515625 14.148438 7.484375 14.148438 8.085938 13.550781 L 13.550781 8.085938 C 14.148438 7.484375 14.148438 6.515625 13.550781 5.914062 Z M 3 7.75 C 2.585938 7.75 2.25 7.414062 2.25 7 C 2.25 6.585938 2.585938 6.25 3 6.25 C 3.414062 6.25 3.75 6.585938 3.75 7 C 3.75 7.414062 3.414062 7.75 3 7.75 Z M 7 11.75 C 6.585938 11.75 6.25 11.414062 6.25 11 C 6.25 10.585938 6.585938 10.25 7 10.25 C 7.414062 10.25 7.75 10.585938 7.75 11 C 7.75 11.414062 7.414062 11.75 7 11.75 Z M 7 7.75 C 6.585938 7.75 6.25 7.414062 6.25 7 C 6.25 6.585938 6.585938 6.25 7 6.25 C 7.414062 6.25 7.75 6.585938 7.75 7 C 7.75 7.414062 7.414062 7.75 7 7.75 Z M 7 3.75 C 6.585938 3.75 6.25 3.414062 6.25 3 C 6.25 2.585938 6.585938 2.25 7 2.25 C 7.414062 2.25 7.75 2.585938 7.75 3 C 7.75 3.414062 7.414062 3.75 7 3.75 Z M 11 7.75 C 10.585938 7.75 10.25 7.414062 10.25 7 C 10.25 6.585938 10.585938 6.25 11 6.25 C 11.414062 6.25 11.75 6.585938 11.75 7 C 11.75 7.414062 11.414062 7.75 11 7.75 Z M 11 7.75";
    private static final String SVGaccept = "M 5.433594 13.730469 L 0.234375 8.53125 C -0.078125 8.21875 -0.078125 7.710938 0.234375 7.398438 L 1.367188 6.269531 C 1.679688 5.957031 2.183594 5.957031 2.496094 6.269531 L 6 9.773438 L 13.503906 2.269531 C 13.816406 1.957031 14.320312 1.957031 14.632812 2.269531 L 15.765625 3.398438 C 16.078125 3.710938 16.078125 4.21875 15.765625 4.53125 L 6.566406 13.730469 C 6.253906 14.042969 5.746094 14.042969 5.433594 13.730469 Z M 5.433594 13.730469";

    private String playerName;
    private PlayerType playerType;
    private Framework framework;
    private Match match;
    private Main main;
    private GameType gameType;

    private Thread runThread = new Thread(this::run);

    private Thread gameThread = new Thread(() -> {
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
        gameThread.start();
    }

    public void start() {
        runThread.start();
    }

    private void run() {
//        while(true) {
//            PlayerList a = framework.getPlayers();
//
//            for (String b : a.getPlayers()) {
//                System.out.println(b);
//            }
//        }

        Platform.runLater( () -> {
            for (int i = 0; i < 5; i++) {
                playersTable.getChildren().add(createPlayerRow(i));
                challengesTable.getChildren().add(createChallengesRow(i));
            }
        });
    }

    private HBox createRow(int i) {
        HBox row = new HBox();
        row.setAlignment(Pos.TOP_CENTER);

        HBox hbPlayerOneText = new HBox();
        hbPlayerOneText.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(hbPlayerOneText, Priority.ALWAYS);
        Text tPlayerOne = new Text("Player Name " + i);
        tPlayerOne.getStyleClass().addAll("h3", "text-dark", "b");
        hbPlayerOneText.getChildren().add(tPlayerOne);

        row.getChildren().add(hbPlayerOneText);

        return row;
    }

    private HBox createPlayerRow(int i) {
        HBox row = createRow(i);

        row.getChildren().add(createButtons(ButtonType.CHALLENGE, i));
        row.setPadding(new Insets(10,0,10,0));

        return row;
    }

    private HBox createChallengesRow(int i) {
        HBox row = createRow(i);

        row.getChildren().add(createButtons(ButtonType.ACCEPT, i));
        row.getChildren().add(createButtons(ButtonType.DECLINE, i));
        row.setPadding(new Insets(10,0,10,0));

        return row;
    }

    private HBox createButton(ButtonType buttonType, int i) {
        HBox button = new HBox();
        button.setAlignment(Pos.CENTER);

        SVGPath buttonImage = new SVGPath();
        buttonImage.getStyleClass().add("text-white");

        HBox.setMargin(buttonImage, new Insets(0, 6.0, 0, 0));

        Text buttonText = new Text();
        buttonText.getStyleClass().addAll("h5", "b", "text-white");

        switch (buttonType) {
            case DECLINE:
                buttonText.setId("Decline " + i);
                buttonText.setText("Decline");
                buttonImage.setContent(SVGcancel);
                button.setOnMouseClicked(this::clickDecline);
                break;
            case CHALLENGE:
                buttonText.setId("Challenge " + i);
                buttonText.setText("Challenge");
                buttonImage.setContent(SVGchallenge);
                button.setOnMouseClicked(this::clickChallenge);
                break;
            case ACCEPT:
                buttonText.setId("Accept " + i);
                buttonText.setText("Accept");
                buttonImage.setContent(SVGaccept);
                button.setOnMouseClicked(this::clickAccept);
                break;
            case CANCEL:
                buttonText.setId("Cancel " + i);
                buttonText.setText("Cancel");
                buttonImage.setContent(SVGcancel);
                button.setOnMouseClicked(this::clickCancel);
                break;
        }

        button.getChildren().add(buttonImage);
        button.getChildren().add(buttonText);

        return button;
    }

    private void checkPlayers() {

    }

    private void checkChallenges() {

    }

    private HBox createButtons(ButtonType buttonType, int i) {
        HBox hbPlayerOneButtons = new HBox();
        hbPlayerOneButtons.setAlignment(Pos.CENTER_RIGHT);
        hbPlayerOneButtons.getStyleClass().add("btn");

        switch (buttonType) {
            case CANCEL:
            case CHALLENGE:
                hbPlayerOneButtons.getStyleClass().add("btn-secondary");
                break;
            case ACCEPT:
                hbPlayerOneButtons.getStyleClass().add("btn-success");
                break;
            case DECLINE:
                hbPlayerOneButtons.getStyleClass().add("btn-danger");
                break;
        }

        hbPlayerOneButtons.getChildren().add(createButton(buttonType, i));
        HBox.setMargin(hbPlayerOneButtons, new Insets(0,10,0,0));

        return hbPlayerOneButtons;
    }

    private void clickChallenge(MouseEvent event) {
        HBox field = (HBox) event.getSource();

        Text text = (Text) field.getChildren().get(1);
        if (text.getText().equals("Cancel")) {
            framework.cancelChallenge();
        } else if (text.getText().equals("Challenge")) {
            framework.sendChallenge(text.getId(), gameType);
        }
    }

    private void clickCancel(MouseEvent event) {

    }

    private void clickAccept(MouseEvent event) {

    }

    private void clickDecline(MouseEvent event) {

    }

    @FXML
    private void leave(ActionEvent event) {
        main.changeToHome();
    }
}
