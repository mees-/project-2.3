package ui.controller;

import ai.Ai;
import framework.*;
import framework.player.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import reversi.ReversiBoard;
import tictactoe.Game;
import ui.Main;
import ui.update.GameStateUpdate;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.paint.Color.BLACK;

public class ReversiController {
    private Framework framework;
    private Main main;
    private Player localPlayerOne, localPlayerTwo;
    private Player currentPlayer;
    private Match match;

    @FXML
    GridPane gpReversi;

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

    private boolean test = false;

    private Thread a;

    public ReversiController(Main main, Framework framework, Player localPlayerOne) {
        this.framework = framework;
        this.main = main;
        this.localPlayerOne = localPlayerOne;
        System.out.println(localPlayerOne.getUsername());
        new Thread(() -> {
            while (match == null) {
                match = framework.getMatch();
                if (match != null) {
                    setupNames();
                    run();
                }
            }
        }).start();
    }

    public ReversiController(Main main, Framework framework, LocalPlayer localPlayerOne, LocalPlayer localPlayerTwo) {
        this.framework = framework;
        this.main = main;
        this.localPlayerOne = localPlayerOne;
        this.localPlayerTwo = localPlayerTwo;
        System.out.println(localPlayerOne.getUsername());
        getMatch();
    }

    public void getMatch() {
        new Thread(() -> {
            while (match == null) {
                match = framework.getMatch();
                if (match != null) {
                    a = new Thread(() -> {
                        match.gameLoop();
                    });
                    a.start();

                    setupNames();
                    run();
                }
            }
        }).start();
    }

    private Circle setupPiece(boolean blackOrWhite) {
        Circle circle = new Circle();
        circle.setRadius(1.0);
        circle.setStroke(BLACK);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(1.5);

        if (!blackOrWhite) {
            circle.getStyleClass().addAll("piece-reversi", "piece-reversi-white");
        } else {
            circle.getStyleClass().addAll("piece-reversi", "piece-reversi-black");
        }

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
        childNodes = gpReversi.getChildren();

        for (Node node : childNodes) {
            if (node instanceof HBox) {
                if (localPlayerTwo == null) {
                    node.getStyleClass().add("tile-reversi-disabled");
                }
                if (!(localPlayerOne instanceof Ai)) {
                    node.setOnMouseClicked((this::mouseClick));
                }
            }
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

            ReversiBoard board = ((ReversiBoard) update.getBoard());
            GameState gameState = update.getGameState();
            int[] score = board.countPieces();
            Platform.runLater( () -> {
                sPlayerTwo.setText(Integer.toString(score[1]));
                sPlayerOne.setText(Integer.toString(score[0]));
            });

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
        framework.clearMatch();
        match = null;
    }

    private void updateBoard(BoardInterface board, GameState turn) {
        for (Node node : childNodes) {
            if (node instanceof HBox) {
                if (turn == GameState.TurnOne && localPlayerTwo == null) {
                    Platform.runLater(() -> node.getStyleClass().remove("tile-reversi-disabled"));
                } else if (turn == GameState.TurnTwo) {
                    if (!node.getStyleClass().contains("tile-reversi-disabled") && localPlayerTwo == null) {
                        Platform.runLater(() -> node.getStyleClass().add("tile-reversi-disabled"));
                    }
                }

                Platform.runLater(() -> node.getStyleClass().remove("tile-reversi-available"));

                CellContent content = board.getCell(GridPane.getColumnIndex(node) - 1, GridPane.getRowIndex(node) - 1);
                HBox hBox = (HBox) node;

                if (content == CellContent.Local) {
                    changeHbox(hBox, false);
                    Platform.runLater( () -> {
                        if (hBox.getChildren().size() == 0) {
                            hBox.getChildren().add((setupPiece(false)));
                        }
                    });
                } else if (content == CellContent.Remote) {
                    changeHbox(hBox, true);
                    Platform.runLater( () -> {
                        if (hBox.getChildren().size() == 0) {
                            hBox.getChildren().add((setupPiece(true)));
                        }
                    });
                }
                if (!(localPlayerOne instanceof Ai)) {
                    if (turn == GameState.TurnOne || (localPlayerTwo != null)) {
                        for (Move move : board.getValidMoves(turn)) {
                            if ((GridPane.getColumnIndex(node) - 1) == move.getX() && (GridPane.getRowIndex(node) - 1) == move.getY()) {
                                Platform.runLater(() -> node.getStyleClass().add("tile-reversi-available"));
                            }
                        }
                    }
                }
            }
        }
    }

    private void changeHbox(HBox hBox, boolean blackOrWhite) {
        String cssClass, cssClassContains;

        if (blackOrWhite) {
            cssClassContains = "piece-reversi-black";
            cssClass = "piece-reversi-white";
        } else {
            cssClassContains = "piece-reversi-white";
            cssClass = "piece-reversi-black";
        }

        if (hBox.getChildren().size() > 0) {
            if (!hBox.getChildren().get(0).getStyleClass().contains(cssClassContains) && (hBox.getChildren().get(0) instanceof Circle)) {
                if (hBox.getChildren().get(0).getStyleClass().contains(cssClass)) {
                    Platform.runLater(() -> {
                        hBox.getChildren().remove(0);
                    });
                }
            }
        }
    }

    private void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());

        if (field.getStyleClass().contains("tile-reversi-available")) {
            Move move = new Move(currentPlayer.getTurn(), (GridPane.getColumnIndex(field) - 1), (GridPane.getRowIndex(field) - 1));
            ((LocalPlayer) currentPlayer).putMove(move);
            test = true;
        }
    }
}
