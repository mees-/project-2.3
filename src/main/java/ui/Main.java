package ui;

import connection.Connection;
import framework.Framework;
import framework.GameState;
import framework.GameType;
import framework.player.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import ui.controller.HomeController;
import ui.controller.ReversiController;
import ui.settings.ChosenGame;
import ui.settings.OnlineOption;
import ui.settings.PlayerType;

import java.io.IOException;
import java.util.Collections;

public class Main extends Application {
    private Pane paneHome, paneReversi, paneTicTacToe;
    private Pane root;
    private Framework framework;

    private ui.settings.GameType gameTypeEnum = ui.settings.GameType.ONLINE;
    private OnlineOption onlineOptionEnum = OnlineOption.SUBSCRIBE;
    private PlayerType playerOneTypeEnum = PlayerType.HUMAN;
    private PlayerType playerTwoTypeEnum = PlayerType.HUMAN;
    private ChosenGame chosenGameEnum = ChosenGame.REVERSI;

    private FXMLLoader loader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Tha Koel Gamebox");
        initUI(stage);
        stage.show();
    }

    private void initUI(Stage stage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/root.fxml"));
        loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
        loader.setController(new HomeController(this, gameTypeEnum, onlineOptionEnum, playerOneTypeEnum, playerTwoTypeEnum, chosenGameEnum));
        paneHome = loader.load();
        root.getChildren().add(paneHome);

        ObservableList<Node> workingCollection = FXCollections.observableArrayList(root.getChildren());
        Collections.swap(workingCollection, 0, 1);
        root.getChildren().setAll(workingCollection);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public void changePane(ChosenGame chosenGame, String playerOneName) throws IOException {
        LocalPlayer playerOne = new LocalPlayer(playerOneName);
        startFramework(playerOne);
        switch (chosenGame) {
            case REVERSI:
                    root.getChildren().remove(paneHome);
                    ReversiController reversiController = new ReversiController(this, framework, playerOne);
                    loader =  new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                    loader.setController(reversiController);
                    paneReversi = loader.load();
                    root.getChildren().add(paneReversi);
                    reversiController.setup();
                    startReversi();
                break;
            case TICTACTOE:
                break;
            default:
                System.out.println("Something went wrong!");
        }
    }

    public void changePane(ChosenGame chosenGame, String playerOneName, String playerTwoName) throws IOException {
        LocalPlayer playerOne = new LocalPlayer(playerOneName);
        LocalPlayer playerTwo = new LocalPlayer(playerTwoName);
        Player test = startFramework(playerOne, playerTwo);
        switch (chosenGame) {
            case REVERSI:
                root.getChildren().remove(paneHome);
                ReversiController reversiController = new ReversiController(this, framework, playerOne, playerTwo);
                loader =  new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                loader.setController(reversiController);
                paneReversi = loader.load();
                root.getChildren().add(paneReversi);
                reversiController.setup();
                startReversiLocal(test);
                break;
            case TICTACTOE:
                break;
            default:
                System.out.println("Something went wrong!");
        }
    }

    public Player startFramework(LocalPlayer playerOne, LocalPlayer playerTwo) {
        Player playerOneLocal = new LocalNotConnectedPlayer(playerOne);
        Player playerTwoLocal = new LocalNotConnectedPlayer(playerTwo);
        framework = new Framework(playerOneLocal, playerTwoLocal);
        return playerTwoLocal;
    }

    public void startFramework(LocalPlayer player) throws IOException {
        Connection connection = new Connection();
        Player connectedPlayer = new LocalConnectedPlayer(player, connection);
        framework = new Framework(connectedPlayer, connection);
        framework.login();
    }

    private void startReversi() {
        new Thread ( () -> {
            framework.runGameSync(GameType.Reversi);
        }).start();
    }

    private void startReversiLocal(Player playerTwo) {
        framework.notifyGameOffer(GameType.Reversi, playerTwo, GameState.TurnOne);
    }

    public void stopFramework() throws IOException {
        framework.close();
    }

}
