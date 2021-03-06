package gui.controller;

import framework.Framework;
import framework.GameType;
import framework.Match;
import framework.player.ChallengePlayer;
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
import gui.Main;
import gui.settings.ButtonType;
import gui.settings.PlayerType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

    private List<String> playerList;
    private List<ChallengePlayer> challengeList;

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
        new Thread(() -> {
            while(true) {
                framework.retrieveChallenges();
            }
        }).start();
    }

    public void start() {
        runThread.start();
    }

    private void run() {
        while(true) {
            List<String> playerListTemp = framework.getPlayers();
            List<ChallengePlayer> challengeListTemp = framework.getChalllenges();

            checkPlayers(playerListTemp);
            checkChallenges(challengeListTemp);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private HBox createRow(String playerName) {
        HBox row = new HBox();
        row.setAlignment(Pos.TOP_CENTER);
        row.setId("row-" + playerName);

        return row;
    }

    private HBox addText(String playerName) {
        HBox hbPlayerText = new HBox();
        hbPlayerText.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(hbPlayerText, Priority.ALWAYS);

        Text tPlayer = new Text(playerName);
        tPlayer.getStyleClass().addAll("h3", "text-dark", "b");

        hbPlayerText.getChildren().add(tPlayer);

        return hbPlayerText;
    }

    private HBox createPlayerRow(String playerName) {
        HBox row = createRow(playerName);

        row.getChildren().add(addText(playerName));

        row.getChildren().add(setupButtonPlayers(ButtonType.CHALLENGE, playerName));
        row.setPadding(new Insets(10,0,10,0));

        return row;
    }

    private HBox createChallengesRow(String playerName, GameType game, Integer challengeNumber) {
        HBox row = createRow(playerName);

        row.getChildren().add(addText(playerName));

        switch (game) {
            case Reversi:
                row.getChildren().add(addText("Reversi"));
                break;
            case TicTacToe:
                row.getChildren().add(addText("Tic-Tac-Toe"));
                break;
        }

        row.getChildren().add(setupButtonChallenges(ButtonType.ACCEPT, challengeNumber));
        row.getChildren().add(setupButtonChallenges(ButtonType.DECLINE, challengeNumber));
        row.setPadding(new Insets(10,0,10,0));

        return row;
    }

    private SVGPath setupButtonImage() {
        SVGPath buttonImage = new SVGPath();
        buttonImage.getStyleClass().add("text-white");

        HBox.setMargin(buttonImage, new Insets(0, 6.0, 0, 0));

        return buttonImage;
    }

    private HBox createButtonPlayers(ButtonType buttonType, String playerName) {
        HBox button = new HBox();
        button.setAlignment(Pos.CENTER);

        SVGPath buttonImage = setupButtonImage();

        Text buttonText = new Text();
        buttonText.getStyleClass().addAll("h5", "b", "text-white");
        buttonText.setId(playerName);

        switch (buttonType) {
            case CHALLENGE:
                buttonText.setText("Challenge");
                buttonImage.setContent(SVGchallenge);
                button.setOnMouseClicked(this::clickChallenge);
                break;
            case CANCEL:
                buttonText.setText("Cancel");
                buttonImage.setContent(SVGcancel);
                button.setOnMouseClicked(this::clickCancel);
                break;
        }

        button.getChildren().add(buttonImage);
        button.getChildren().add(buttonText);

        return button;
    }

    private HBox createButtonChallenges(ButtonType buttonType, Integer challengeNumber) {
        HBox button = new HBox();
        button.setAlignment(Pos.CENTER);

        SVGPath buttonImage = setupButtonImage();

        Text buttonText = new Text();
        buttonText.getStyleClass().addAll("h5", "b", "text-white");
        buttonText.setId(Integer.toString(challengeNumber));

        switch (buttonType) {
            case DECLINE:
                buttonText.setText("Decline");
                buttonImage.setContent(SVGcancel);
                button.setOnMouseClicked(this::clickDecline);
                break;
            case ACCEPT:
                buttonText.setText("Accept");
                buttonImage.setContent(SVGaccept);
                button.setOnMouseClicked(this::clickAccept);
                break;
        }

        button.getChildren().add(buttonImage);
        button.getChildren().add(buttonText);

        return button;
    }

    private void checkPlayers(List<String> playerListTemp) {
        if (!playerListTemp.equals(playerList)) {
            playerList = Collections.unmodifiableList(playerListTemp);

            Platform.runLater(() -> {
                while (playersTable.getChildren().size() > 0) {
                    playersTable.getChildren().remove(0);
                }
                for (String name : playerList) {
                    if (!name.equals(playerName)) {
                        playersTable.getChildren().add(createPlayerRow(name));
                    }
                }
            });
        }
    }

    private void checkChallenges(List<ChallengePlayer> challengeListTemp) {
        if (!challengeListTemp.equals(challengeList) && challengeListTemp.size() > 0) {
            challengeList = Collections.unmodifiableList(challengeListTemp);

            Platform.runLater(() -> {
                while (challengesTable.getChildren().size() > 0) {
                    challengesTable.getChildren().remove(0);
                }
                for (ChallengePlayer player : challengeList) {
                    challengesTable.getChildren().add(createChallengesRow(player.getUsername(), player.getGameType(), player.getChallengeNumber()));
                }
            });
        }
    }

    private HBox setupButtonPlayers(ButtonType buttonType, String playerName) {
        HBox hbPlayerOneButtons = new HBox();
        hbPlayerOneButtons.setAlignment(Pos.CENTER_RIGHT);
        hbPlayerOneButtons.getStyleClass().add("btn");

        hbPlayerOneButtons.getStyleClass().add("btn-secondary");

        hbPlayerOneButtons.getChildren().add(createButtonPlayers(buttonType, playerName));
        HBox.setMargin(hbPlayerOneButtons, new Insets(0,10,0,0));

        return hbPlayerOneButtons;
    }

    private HBox setupButtonChallenges(ButtonType buttonType, Integer challengeNumber) {
        HBox hbChallengeButtons = new HBox();
        hbChallengeButtons.setAlignment(Pos.CENTER_RIGHT);
        hbChallengeButtons.getStyleClass().add("btn");

        switch (buttonType) {
            case ACCEPT:
                hbChallengeButtons.getStyleClass().add("btn-success");
                break;
            case DECLINE:
                hbChallengeButtons.getStyleClass().add("btn-danger");
                break;
        }

        hbChallengeButtons.getChildren().add(createButtonChallenges(buttonType, challengeNumber));
        HBox.setMargin(hbChallengeButtons, new Insets(0,10,0,0));

        return hbChallengeButtons;
    }

    private void clickChallenge(MouseEvent event) {
        HBox field = (HBox) event.getSource();

        Text text = (Text) field.getChildren().get(1);
        framework.sendChallenge(text.getId(), gameType);

        if (text.getText().equals("Challenge")) {
            Platform.runLater( () -> {
                HBox parent = (HBox) text.getParent().getParent().getParent();
                parent.getChildren().remove(1);
            });
        }
    }

    private void clickAccept(MouseEvent event) {
        HBox field = (HBox) event.getSource();

        Text text = (Text) field.getChildren().get(1);
        int id = Integer.parseInt(text.getId());

        if (text.getText().equals("Accept")) {
            Platform.runLater( () -> {
                HBox row = (HBox) text.getParent().getParent().getParent();
                VBox parent = (VBox) row.getParent();
                System.out.println(row.getId());

                // knop moet terugkomen, de challenge is namelijk voorbij
                HBox playerRow = (HBox) playersTable.lookup("#" + row.getId());
                HBox test = (HBox) row.getChildren().get(0);
                Text veldname = (Text) test.getChildren().get(0);
                System.out.println(veldname.getText());

                playerRow.getChildren().add(createButtonPlayers(
                        ButtonType.CHALLENGE,
                        veldname.getText()
                ));

                parent.getChildren().remove(row);

                framework.acceptChallenge(id);
            });
        }
    }

    private void clickCancel(MouseEvent event) {
        System.out.println("Cancel");
    }

    private void clickDecline(MouseEvent event) {

    }

    @FXML
    private void leave(ActionEvent event) {
        main.changeToHome();
    }
}
