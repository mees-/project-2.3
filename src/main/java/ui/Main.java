package ui;

import connection.Connection;
import framework.Framework;
import framework.GameType;
import framework.player.LocalConnectedPlayer;
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
    private static Pane currentPane;
    private static Pane root;
    private static Framework framework;

    public static ui.settings.GameType gameTypeEnum;
    public static OnlineOption onlineOptionEnum;
    public static PlayerType playerOneTypeEnum;
    public static PlayerType playerTwoTypeEnum;
    public static ChosenGame chosenGameEnum;

    public static void main(String[] args) {
        gameTypeEnum = ui.settings.GameType.LOCAL;
        onlineOptionEnum = OnlineOption.SUBSCRIBE;
        playerOneTypeEnum = PlayerType.HUMAN;
        playerTwoTypeEnum = PlayerType.HUMAN;
        chosenGameEnum = ChosenGame.REVERSI;

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
        currentPane = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
        root.getChildren().add(currentPane);

        ObservableList<Node> workingCollection = FXCollections.observableArrayList(root.getChildren());
        Collections.swap(workingCollection, 0, 1);
        root.getChildren().setAll(workingCollection);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public static void changePane() throws IOException {
        switch (chosenGameEnum) {
            case REVERSI:
                root.getChildren().remove(currentPane);
                root.getChildren().add(FXMLLoader.load(Main.class.getResource("/view/reversi.fxml")));
                startFramework();
                break;
            case TICTACTOE:
                break;
            default:
                System.out.println("Something went wrong!");
        }
    }

    public static void startFramework() throws IOException {
        Connection connection = new Connection();
        Player player = new LocalConnectedPlayer(new RandomMovePlayer(), connection);
        framework = new Framework(player, connection);
        framework.login();

        new Thread ( () -> {
            framework.runGameSync(GameType.Reversi);
        });
    }

    public static void stopFramework() throws IOException {
        framework.close();
    }

}
