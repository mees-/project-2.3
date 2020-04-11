package ui.controller;

import ai.Ai;
import framework.*;
import framework.player.BlockingPlayer;
import framework.player.UIPlayer;
import framework.player.Player;
import framework.player.Players;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import tictactoe.Board;
import ui.Main;
import ui.update.GameStateUpdate;

import static javafx.scene.paint.Color.BLACK;

public class TicTacToeController {
    private Framework framework;
    private Main main;
    private Player localPlayerOne, localPlayerTwo;
    private Player currentPlayer;
    private Match match;

    @FXML
    GridPane gpTicTacToe;

    @FXML
    Text txtPlayerOne;

    @FXML
    Text txtPlayerTwo;

    @FXML
    SVGPath wcPlayerOne;

    @FXML
    SVGPath wcPlayerTwo;

    @FXML
    SVGPath tPlayerOne;

    @FXML
    SVGPath tPlayerTwo;

    @FXML
    Text sPlayerOne;

    @FXML
    Text sPlayerTwo;

    private ObservableList<Node> childNodes;

    private Players players;

    private Thread a;

    public TicTacToeController(Main main, Framework framework, Player localPlayerOne) {
        this.framework = framework;
        this.main = main;
        this.localPlayerOne = localPlayerOne;
        System.out.println(localPlayerOne.getUsername());
        getMatch();
    }

    public TicTacToeController(Main main, Match match, UIPlayer localPlayerOne, UIPlayer localPlayerTwo) {
        this.localPlayerOne = localPlayerOne;
        this.localPlayerTwo = localPlayerTwo;
        this.match = match;
        new Thread(() -> {
            setupNames();
            run();
        }).start();
    }

    public void getMatch() {
        new Thread(() -> {
            while (match == null) {
                match = framework.getMatch();
                if (match != null) {
                    match.startAsync();
                    setupNames();
                    run();
                }
            }
        }).start();
    }

    private SVGPath createCross() {
        SVGPath cross = new SVGPath();
        cross.setContent("M 100.671875 106 L 142.179688 64.566406 C 147.273438 59.480469 147.273438 51.234375 142.179688 46.148438 L 132.957031 36.9375 C 127.863281 31.855469 119.605469 31.855469 114.507812 36.9375 L 73 78.375 L 31.492188 36.9375 C 26.398438 31.855469 18.140625 31.855469 13.042969 36.9375 L 3.820312 46.148438 C -1.273438 51.230469 -1.273438 59.476562 3.820312 64.566406 L 45.328125 106 L 3.820312 147.433594 C -1.273438 152.519531 -1.273438 160.765625 3.820312 165.851562 L 13.042969 175.0625 C 18.136719 180.144531 26.398438 180.144531 31.492188 175.0625 L 73 133.625 L 114.507812 175.0625 C 119.601562 180.144531 127.863281 180.144531 132.957031 175.0625 L 142.179688 165.851562 C 147.273438 160.769531 147.273438 152.523438 142.179688 147.433594 Z M 100.671875 106 ");
        cross.getStyleClass().add("piece-tictactoe-cross");

        return cross;
    }

    private SVGPath createCrossHover() {
        SVGPath cross = createCross();
        cross.getStyleClass().add("piece-tictactoe-possible-move");

        return cross;
    }

    private Circle createCircle() {
        Circle circle = new Circle();
        circle.setRadius(1.0);
        circle.setStroke(BLACK);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(1.5);
        circle.getStyleClass().addAll("piece-tictactoe-circle-base", "piece-tictactoe-circle");

        return circle;
    }

    public void setupNames() {
        Platform.runLater(() -> {
            new Thread(() -> {
                players = match.getPlayers();
                txtPlayerOne.setText(players.one.getUsername());
                txtPlayerTwo.setText(players.two.getUsername());
            }).start();
        });
    }

    public void setup() {
        childNodes = gpTicTacToe.getChildren();

        for (Node node : childNodes) {
            if (node instanceof HBox) {
                if (localPlayerTwo == null) {
                    node.getStyleClass().add("tile-tictactoe-disabled");
                }
                if (!(localPlayerOne instanceof Ai)) {
                    node.setOnMouseClicked(this::mouseClick);
                    node.setOnMouseEntered(this::mouseHoverIn);
                    node.setOnMouseExited(this::mouseHoverOut);
                }
            }
        }
    }

    private void mouseHoverIn(Event event) {
        HBox hBox = (HBox) event.getSource();
        if (hBox.getChildren().size() == 0) {
            hBox.getChildren().add(createCrossHover());
        }
    }

    private void mouseHoverOut(MouseEvent event) {
        HBox hBox = (HBox) event.getSource();
        if (hBox.getChildren().size() > 0) {
            hBox.getChildren().remove(0);
        }
    }

    public void run() {
        while (match != null) {
            GameStateUpdate update;

            try {
                update = match.getGameUpdate();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Board board = ((Board) update.getBoard());
            GameState gameState = update.getGameState();

            if (gameState == GameState.TurnOne || gameState == GameState.TurnTwo) {
                if (gameState == GameState.TurnOne) {
                    currentPlayer = localPlayerOne;
                    tPlayerOne.setVisible(true);
                    tPlayerTwo.setVisible(false);
                } else {
                    currentPlayer = localPlayerTwo;
                    tPlayerOne.setVisible(false);
                    tPlayerTwo.setVisible(true);
                }

                updateBoard(board, gameState);
            } else if (gameState == GameState.OneWin) {
                wcPlayerOne.setVisible(true);
                updateBoard(board, GameState.TurnOne);
                resetMatch();
            } else if (gameState == GameState.TwoWin) {
                wcPlayerTwo.setVisible(true);
                updateBoard(board, GameState.TurnTwo);
                resetMatch();
            } else if (gameState == GameState.Draw) {
                // ???
                resetMatch();
            } else {

            }
        }
    }

    private void resetMatch() {
        match = null;
    }

    private void updateBoard(BoardInterface board, GameState turn) {
        for (Node node : childNodes) {
            if (node instanceof HBox) {
                if (turn == GameState.TurnOne && localPlayerTwo == null) {
                    Platform.runLater(() -> node.getStyleClass().remove("tile-tictactoe-disabled"));
                } else if (turn == GameState.TurnTwo) {
                    if (!node.getStyleClass().contains("tile-tictactoe-disabled") && localPlayerTwo == null) {
                        Platform.runLater(() -> node.getStyleClass().add("tile-tictactoe-disabled"));
                    }
                }

                CellContent content = board.getCell(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
                HBox hBox = (HBox) node;

                if (content == CellContent.Local) {
                    Platform.runLater( () -> {
                        if (hBox.getChildren().size() == 0) {
                            hBox.getChildren().add((createCross()));
                        }
                    });
                } else if (content == CellContent.Remote) {
                    Platform.runLater( () -> {
                        if (hBox.getChildren().size() == 0) {
                            hBox.getChildren().add((createCircle()));
                        }
                    });
                }
            }
        }
    }

    private void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());
        System.out.println("click");

        Move move = new Move(currentPlayer.getTurn(), (GridPane.getColumnIndex(field)), (GridPane.getRowIndex(field)));
        ((BlockingPlayer) currentPlayer).putMove(move);
    }
}
