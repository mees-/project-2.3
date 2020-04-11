package ui;

import ai.ReversiAi;
import ai.TicTacToeAi;
import connection.Connection;
import framework.Framework;
import framework.GameState;
import framework.GameType;
import framework.Match;
import framework.player.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import reversi.ReversiGame;
import tictactoe.Game;
import ui.controller.HomeController;
import ui.controller.ReversiController;
import ui.controller.TicTacToeController;
import ui.settings.OnlineOption;
import ui.settings.PlayType;
import ui.settings.PlayerType;
import java.io.IOException;

public class Main extends Application {
    private Pane paneHome, paneReversi, paneTicTacToe, paneFooter;
    private Pane currentPane;
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
        setCurrentPane(paneHome);

        paneFooter = FXMLLoader.load(getClass().getResource("/view/components/footer.fxml"));
        root.getChildren().add(paneFooter);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public void changePane(GameType chosenGame, String playerOneName, PlayerType playerType) throws IOException {
        root.getChildren().remove(paneHome);

        Player playerOne = new LocalPlayer(playerOneName, chosenGame);

        switch (chosenGame) {
            case Reversi:
                if (playerType == PlayerType.AI) {
                    playerOne = new ReversiAi(playerOneName);
                }

                startFramework(playerOne);
                startReversi();
                ReversiController reversiController = null;
                try {
                    reversiController = new ReversiController(this, framework.getMatchFuture().get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                loader =  new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                loader.setController(reversiController);
                paneReversi = loader.load();
                setCurrentPane(paneReversi);

                reversiController.setup();
                reversiController.start();

                break;
            case TicTacToe:
                if (playerType == PlayerType.AI) {
                    playerOne = new TicTacToeAi(playerOneName);
                }

                startFramework(playerOne);
                root.getChildren().remove(paneHome);
                TicTacToeController ticTacToeController = new TicTacToeController(this, framework, playerOne);
                loader =  new FXMLLoader(getClass().getResource("/view/ticTacToe.fxml"));
                loader.setController(ticTacToeController);
                paneTicTacToe = loader.load();
                root.getChildren().add(paneTicTacToe);
                ticTacToeController.setup();
                startTicTacToe();
                break;
            default:
                System.out.println("Something went wrong!");
        }

//        ObservableList<Node> workingCollection = FXCollections.observableArrayList(root.getChildren());
//        Collections.swap(workingCollection,0,1);
//        root.getChildren().setAll(workingCollection);
    }

    public void changePane(GameType gameType, String playerOneName, String playerTwoName, PlayerType playerTypeOne, PlayerType playerTypeTwo) throws IOException {
        Player playerOne = new LocalPlayer(playerOneName, gameType);
        Player playerTwo = new LocalPlayer(playerTwoName, gameType);
        Match match = null;
        root.getChildren().remove(paneFooter);
        root.getChildren().remove(paneHome);

        switch (gameType) {
            case Reversi:
                if (playerTypeOne == PlayerType.AI) {
                    playerOne = new ReversiAi(playerOneName);
                }
                if (playerTypeTwo == PlayerType.AI) {
                    playerTwo = new ReversiAi(playerTwoName);
                }

                match = new Match(new ReversiGame(), playerOne, playerTwo);
                ReversiController reversiController = new ReversiController(this, match);

                loader = new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                loader.setController(reversiController);
                paneReversi = loader.load();
                setCurrentPane(paneReversi);

                reversiController.setup();
                reversiController.start();

                break;
            case TicTacToe:
//                match = new Match(new Game(), playerOne, playerTwo);
//                root.getChildren().remove(paneHome);
//                TicTacToeController ticTacToeController = new TicTacToeController(this, match, playerOne, playerTwo);
//                loader =  new FXMLLoader(getClass().getResource("/view/ticTacToe.fxml"));
//                loader.setController(ticTacToeController);
//                paneTicTacToe = loader.load();
//                root.getChildren().add(paneTicTacToe);
//                ticTacToeController.setup();
                break;
            default:
                System.out.println("Something went wrong!");
        }

        match.setupGame(GameState.TurnOne);
        match.startAsync();

        root.getChildren().add(paneFooter);
    }

    public void forfeit() {
        try {
            framework.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Pane getCurrentPane() {
        return currentPane;
    }

    private void setCurrentPane(Pane newCurrentPane) {
        Platform.runLater( () -> {
            root.getChildren().remove(getCurrentPane());
            this.currentPane = newCurrentPane;
            root.getChildren().add(newCurrentPane);
            root.getChildren().remove(paneFooter);
            root.getChildren().add(paneFooter);
        });
    }

    public void changeToHome() {
        setCurrentPane(paneHome);
    }

    public void startFramework(Player player) throws IOException {
        connection = new Connection();
        Player connectedPlayer = new LocalConnectedPlayer(player, connection);
        framework = new Framework(connectedPlayer, connection);
    }

    private void startReversi() {
        connection.subscribe(GameType.Reversi);
    }

    private void startTicTacToe() {
        connection.subscribe(GameType.TicTacToe);
    }

    public void stopFramework() throws IOException {
        framework.close();
    }

}
