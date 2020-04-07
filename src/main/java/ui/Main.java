package ui;

import connection.Connection;
import framework.Framework;
import framework.GameType;
import framework.player.LocalConnectedPlayer;
import framework.player.LocalPlayer;
import framework.player.Player;
import framework.player.RandomMovePlayer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import ui.controller.HomeController;
import ui.settings.ChosenGame;
import ui.settings.OnlineOption;
import ui.settings.PlayerType;

import java.io.IOException;
import java.util.Collections;

public class Main extends Application {
    private Pane currentPane;
    private Pane root;
    private Framework framework;

    private ui.settings.GameType gameTypeEnum = ui.settings.GameType.LOCAL;
    private OnlineOption onlineOptionEnum = OnlineOption.SUBSCRIBE;
    private PlayerType playerOneTypeEnum = PlayerType.HUMAN;
    private PlayerType playerTwoTypeEnum = PlayerType.HUMAN;
    private ChosenGame chosenGameEnum = ChosenGame.REVERSI;

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
        FXMLLoader fxmlHome = new FXMLLoader(getClass().getResource("/view/home.fxml"));
        fxmlHome.setController(new HomeController(this, gameTypeEnum, onlineOptionEnum, playerOneTypeEnum, playerTwoTypeEnum, chosenGameEnum));
        currentPane = fxmlHome.load();
        root.getChildren().add(currentPane);

        ObservableList<Node> workingCollection = FXCollections.observableArrayList(root.getChildren());
        Collections.swap(workingCollection, 0, 1);
        root.getChildren().setAll(workingCollection);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public void changePane(ChosenGame chosenGame, String playerName) throws IOException {
        switch (chosenGame) {
            case REVERSI:
                root.getChildren().remove(currentPane);
                root.getChildren().add(FXMLLoader.load(Main.class.getResource("/view/reversi.fxml")));
                startFramework(playerName);
                break;
            case TICTACTOE:
                break;
            default:
                System.out.println("Something went wrong!");
        }
    }

    public void startFramework(String playerName) throws IOException {
        Connection connection = new Connection();
        Player player = new LocalConnectedPlayer(new LocalPlayer(playerName), connection);
        framework = new Framework(player, connection);
        framework.login();

        new Thread ( () -> {
            framework.runGameSync(GameType.Reversi);
        });
    }

    public void stopFramework() throws IOException {
        framework.close();
    }

}
