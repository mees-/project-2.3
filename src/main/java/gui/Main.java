package gui;

import ai.ReversiAi;
import ai.TicTacToeAi;
import connection.Connection;
import connection.commands.LogoutCommand;
import framework.*;
import framework.player.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import reversi.ReversiGame;
import tictactoe.TicTacToeGame;
import gui.controller.*;
import gui.settings.PlayType;
import gui.settings.PlayerType;
import java.io.IOException;

public class Main extends Application {
    private Pane paneHome, paneGame, paneHeader, paneFooter, paneLobby;
    private Pane currentPane, previousPane;
    private Pane root;
    private Framework framework;
    private Connection connection;

    private HeaderController headerController;
    private GameController gameController;
    private HomeController homeController;
    private LobbyController lobbyController;

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

    @Override
    public void stop() throws Exception {
        super.stop();
        if (framework != null) {
            connection.executeCommand(new LogoutCommand()).waitForResolve();
            framework.close();
        }
    }

    private void initUI(Stage stage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/root.fxml"));
        loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
        homeController = new HomeController(this);
        loader.setController(homeController);
        paneHome = loader.load();

        loader = new FXMLLoader(getClass().getResource("/view/components/header.fxml"));
        headerController = new HeaderController();
        loader.setController(headerController);
        paneHeader = loader.load();

        paneFooter = FXMLLoader.load(getClass().getResource("/view/components/footer.fxml"));

        setCurrentPane(paneHome);

        root.getChildren().remove(paneHeader);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
//        stage.setFullScreen(true);
    }

    public void lobbySetup(GameType gameType, String playerName, PlayerType playerType) throws IOException {
        startFramework(createPlayer(playerName, playerType, gameType));
        lobbyController = new LobbyController(this, framework, playerName, playerType, gameType);
        loader = new FXMLLoader(getClass().getResource("/view/lobby.fxml"));
        loader.setController(lobbyController);
        paneLobby = loader.load();
        setCurrentPane(paneLobby);
        String subTitle = "Lobby";

        switch (gameType) {
            case Reversi:
                setHeaderReversi(subTitle);
                break;
            case TicTacToe:
                setHeaderTicTacToe(subTitle);
                break;
            default:
                System.out.println("Something went wrong!");
        }


        lobbyController.tournamentSetup();
        lobbyController.start();
    }

    private Player createPlayer(String playerName, PlayerType playerType, GameType gameType) {
        Player player = new BlockingPlayer(playerName, gameType);

        switch(playerType) {
            case AI:
                switch (gameType) {
                    case Reversi:
                        player = new ReversiAi(playerName);
                        break;
                    case TicTacToe:
                        player = new TicTacToeAi(playerName);
                        break;
                }
                break;
            case HUMAN:
                player = new UIPlayer(player);
                break;
        }


        return player;
    }

    public void gameSetupSubscribe(GameType gameType, String playerName, PlayerType playerType) throws IOException {
        startFramework(createPlayer(playerName, playerType, gameType));
        switch (gameType) {
            case Reversi:
                startReversi();
                break;
            case TicTacToe:
                startTicTacToe();
                break;
        }

        GameController gameController = onlineGameControllerSetup(gameType);
        new Thread(() -> {
            try {
                gameController.setup(framework.getNextMatch());
                gameController.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void gameSetupLobby(GameType gameType, Match match) throws IOException {
        gameController = onlineGameControllerSetup(gameType);
        gameController.setup(match);
        gameController.start();
    }

    public GameController onlineGameControllerSetup(GameType gameType) throws IOException {
        GameController gameController = null;
        String subTitle = "Online Game";

        switch (gameType) {
            case Reversi:
                gameController = new ReversiController(this);
                loader = new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                setHeaderReversi(subTitle);
                break;
            case TicTacToe:
                gameController = new TicTacToeController(this);
                loader =  new FXMLLoader(getClass().getResource("/view/tictactoe.fxml"));
                setHeaderTicTacToe(subTitle);
                break;
            default:
                System.out.println("Something went wrong!");
        }

        loader.setController(gameController);
        paneGame = loader.load();
        setCurrentPane(paneGame);

        return gameController;
    }

    public void localGameSetup(GameType gameType, String playerOneName, String playerTwoName, PlayerType playerOneType, PlayerType playerTwoType) throws IOException {
        GameInterface game = null;
        String subTitle = "Local Game";

        switch (gameType) {
            case Reversi:
                game = new ReversiGame();
                gameController = new ReversiController(this);
                loader = new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                setHeaderReversi(subTitle);
                break;
            case TicTacToe:
                game = new TicTacToeGame();
                gameController = new TicTacToeController(this);
                loader =  new FXMLLoader(getClass().getResource("/view/tictactoe.fxml"));
                setHeaderTicTacToe(subTitle);
                break;
            default:
                System.out.println("Something went wrong!");
        }

        Match match = new Match(
                game,
                createPlayer(playerOneName, playerOneType, gameType),
                createPlayer(playerTwoName, playerTwoType, gameType)
        );

        loader.setController(gameController);
        paneGame = loader.load();
        setCurrentPane(paneGame);

        gameController.setup(match);
        gameController.start();

        match.setupGame(GameState.TurnOne);
        match.startAsync();
    }

    // TODO: this is definitely not how you forfeit. Use framework.ForfeitMove with a player.
    public void forfeit() {

    }

    private Pane getCurrentPane() {
        return currentPane;
    }

    private void setCurrentPane(Pane newCurrentPane) {
        Platform.runLater( () -> {
            previousPane = getCurrentPane();
            root.getChildren().removeAll(paneHeader, paneFooter, previousPane);
            this.currentPane = newCurrentPane;

            if (paneHome != newCurrentPane) {
                root.getChildren().add(paneHeader);
            }
            root.getChildren().add(newCurrentPane);
            root.getChildren().add(paneFooter);
        });
    }

    public void changePane() {
        paneGame = null;

        if (previousPane == paneLobby) {
            setCurrentPane(paneLobby);
        } else if (previousPane == paneHome) {
            setCurrentPane(paneHome);

            if (homeController.getPlayTypeEnum() == PlayType.ONLINE) {
                try {
                    stopSubscribe();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void changeToHome() {
        previousPane = paneHome;
        changePane();
    }

    public void startFramework(Player player) throws IOException {
        connection = new Connection();
        framework = new Framework(new LocalConnectedPlayer(player, connection), connection);
    }

    private void startReversi() {
        connection.subscribe(GameType.Reversi);
    }

    private void startTicTacToe() {
        connection.subscribe(GameType.TicTacToe);
    }

    public void stopSubscribe() throws IOException, InterruptedException {
        framework.close();
    }

    private void setHeaderReversi(String subTitle) {
        headerController.setup("Reversi", subTitle);
    }

    private void setHeaderTicTacToe(String subTitle) {
        headerController.setup("Tic-Tac-Toe", subTitle);
    }
}
