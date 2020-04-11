package ui;

import ai.ReversiAi;
import connection.Connection;
import framework.Framework;
import framework.GameType;
import framework.player.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import ui.controller.HomeController;
import ui.controller.ReversiController;
import ui.settings.OnlineOption;
import ui.settings.PlayType;
import ui.settings.PlayerType;

import java.io.IOException;
import java.util.Collections;

public class Main extends Application {
    private Pane paneHome, paneReversi, paneTicTacToe;
    private Pane root;
    private Framework framework;
    private Connection connection;

    private PlayType playTypeEnum = PlayType.ONLINE;
    private OnlineOption onlineOptionEnum = OnlineOption.SUBSCRIBE;
    private PlayerType playerOneTypeEnum = PlayerType.HUMAN;
    private PlayerType playerTwoTypeEnum = PlayerType.HUMAN;
    private GameType chosenGameEnum = GameType.Reversi;

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
        loader.setController(new HomeController(this, playTypeEnum, onlineOptionEnum, playerOneTypeEnum, playerTwoTypeEnum, chosenGameEnum));
        paneHome = loader.load();
        root.getChildren().add(paneHome);

        ObservableList<Node> workingCollection = FXCollections.observableArrayList(root.getChildren());
        Collections.swap(workingCollection, 0, 1);
        root.getChildren().setAll(workingCollection);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public void changePane(GameType chosenGame, String playerOneName, PlayerType playerType) throws IOException {
        Player playerOne = null;
        if (playerType == PlayerType.HUMAN) {
            playerOne = new LocalPlayer(playerOneName, chosenGame);
        } else if (playerType == PlayerType.AI) {
            playerOne = new ReversiAi(playerOneName);
        }

        startFramework(playerOne);
        switch (chosenGame) {
            case Reversi:
                    root.getChildren().remove(paneHome);
                    ReversiController reversiController = new ReversiController(this, framework, playerOne);
                    loader =  new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                    loader.setController(reversiController);
                    paneReversi = loader.load();
                    root.getChildren().add(paneReversi);
                    reversiController.setup();
                    startReversi();
                break;
            case TicTacToe:
                break;
            default:
                System.out.println("Something went wrong!");
        }
    }

    public void changePane(GameType chosenGame, String playerOneName, String playerTwoName) throws IOException {
        LocalPlayer playerOne = new LocalPlayer(playerOneName, chosenGame);
        LocalPlayer playerTwo = new LocalPlayer(playerTwoName, chosenGame);
//        Player test = startFramework(playerOne, playerTwo);
        switch (chosenGame) {
            case Reversi:
                root.getChildren().remove(paneHome);
                ReversiController reversiController = new ReversiController(this, framework, playerOne, playerTwo);
                loader =  new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                loader.setController(reversiController);
                paneReversi = loader.load();
                root.getChildren().add(paneReversi);
                reversiController.setup();
//                startReversiLocal(test);
                break;
            case TicTacToe:
                break;
            default:
                System.out.println("Something went wrong!");
        }
    }

    public void startFramework(Player player) throws IOException {
        connection = new Connection();
        Player connectedPlayer = new LocalConnectedPlayer(player, connection);
        framework = new Framework(connectedPlayer, connection);
    }

    private void startReversi() {
        connection.subscribe(GameType.Reversi);
    }

    public void stopFramework() throws IOException {
        framework.close();
    }

}
