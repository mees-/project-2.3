package ui;

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
import ui.controller.*;
import ui.settings.OnlineOption;
import ui.settings.PlayType;
import ui.settings.PlayerType;
import java.io.IOException;

public class Main extends Application {
    private Pane paneHome, paneGame, paneFooter, paneLobby;
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

    private boolean tournament;

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
        loader.setController(new HomeController(this, playTypeEnum, onlineOptionEnum, playerOneTypeEnum, playerTwoTypeEnum, chosenGameEnum));
        paneHome = loader.load();
        setCurrentPane(paneHome);

        paneFooter = FXMLLoader.load(getClass().getResource("/view/components/footer.fxml"));
        root.getChildren().add(paneFooter);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public void lobbySetup(GameType gameType, String playerName, PlayerType playerType) throws IOException {
        startFramework(createPlayer(playerName, playerType, gameType, true));
        LobbyController lobbyController = new LobbyController(this, framework, playerName, playerType);
        loader = new FXMLLoader(getClass().getResource("/view/lobby.fxml"));
        loader.setController(lobbyController);
        paneLobby = loader.load();
        setCurrentPane(paneLobby);

        lobbyController.tournamentSetup();
    }

    private Player createPlayer(String playerName, PlayerType playerType, GameType gameType, boolean uiPlayer) {
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
                if (uiPlayer) {
                    player = new UIPlayer(player);
                }
                break;
        }


        return player;
    }

    public void changeToLobby() {
        setTournament(false);
        setCurrentPane(paneLobby);
    }

    public boolean isTournament() {
        return tournament;
    }

    public void setTournament(boolean tournament) {
        this.tournament = tournament;
    }

    public void onlineGameSetup(GameType gameType, String playerName, PlayerType playerType) throws IOException {
        GameController gameController = null;
        if (!isTournament()) {
            startFramework(createPlayer(playerName, playerType, gameType, false));
        }

        switch (gameType) {
            case Reversi:
                if (!isTournament()) {
                    startReversi();
                }
                gameController = new ReversiController(this);
                loader =  new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                break;
            case TicTacToe:
                if (!isTournament()) {
                    startTicTacToe();
                }
                gameController = new TicTacToeController(this);
                loader =  new FXMLLoader(getClass().getResource("/view/tictactoe.fxml"));
                break;
            default:
                System.out.println("Something went wrong!");
        }

        loader.setController(gameController);
        paneGame = loader.load();
        setCurrentPane(paneGame);

        try {
            gameController.setup(framework.getMatchFuture().get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameController.start();
    }

    public void localGameSetup(GameType gameType, String playerOneName, String playerTwoName, PlayerType playerOneType, PlayerType playerTwoType) throws IOException {
        GameController gameController = null;
        GameInterface game = null;

        switch (gameType) {
            case Reversi:
                game = new ReversiGame();
                gameController = new ReversiController(this);
                loader = new FXMLLoader(getClass().getResource("/view/reversi.fxml"));
                break;
            case TicTacToe:
                game = new TicTacToeGame();
                gameController = new TicTacToeController(this);
                loader =  new FXMLLoader(getClass().getResource("/view/tictactoe.fxml"));
                break;
            default:
                System.out.println("Something went wrong!");
        }

        Match match = new Match(
                game,
                createPlayer(playerOneName, playerOneType, gameType, true),
                createPlayer(playerTwoName, playerTwoType, gameType, true)
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
        if (player instanceof BlockingPlayer) {
            connectedPlayer = new UIPlayer(connectedPlayer);
        }

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
