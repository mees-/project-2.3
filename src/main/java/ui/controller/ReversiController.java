package ui.controller;

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
import tictactoe.Game;
import ui.Main;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.paint.Color.BLACK;

public class ReversiController {
    private Framework framework;
    private Main main;
    private LocalPlayer localPlayer;
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

    public ReversiController(Main main, Framework framework, LocalPlayer localPlayer) {
        this.framework = framework;
        this.main = main;
        this.localPlayer = localPlayer;

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
                node.getStyleClass().add("tile-reversi-disabled");
                node.setOnMouseClicked((this::mouseClick));
            }
        }
    }

    public void run() {
        BoardInterface board = null;
        while (match != null) {
            if (board != match.getGame().getBoard()) {
                if (match.getGameState() == GameState.TurnOne) {
                    board = match.getGame().getBoard();
                    Set<Move> possibleMoves = board.getValidMoves(GameState.TurnOne);

                    for (Node node : childNodes) {
                        if (node instanceof HBox) {
                            node.getStyleClass().remove("tile-reversi-disabled");

                            CellContent content = board.getCell(GridPane.getColumnIndex(node) - 1, GridPane.getRowIndex(node) - 1);
                            HBox hBox = (HBox) node;

                            if (content == CellContent.Local) {
                                if (hBox.getChildren().size() > 0) {
                                    if (!hBox.getChildren().get(0).getStyleClass().contains("piece-reversi-white") && (hBox.getChildren().get(0) instanceof Circle)) {
                                        if (hBox.getChildren().get(0).getStyleClass().contains("piece-reversi-black")) {
                                            hBox.getChildren().remove(0);
                                        }
                                    }
                                }
                                Platform.runLater(() -> {
                                    hBox.getChildren().add((setupPiece(false)));
                                });
                            } else if (content == CellContent.Remote) {
                                if (hBox.getChildren().size() > 0) {
                                    if (!hBox.getChildren().get(0).getStyleClass().contains("piece-reversi-black") && (hBox.getChildren().get(0) instanceof Circle)) {
                                        if (hBox.getChildren().get(0).getStyleClass().contains("piece-reversi-white")) {
                                            hBox.getChildren().remove(0);
                                        }
                                    }
                                }
                                Platform.runLater(() -> {
                                    hBox.getChildren().add((setupPiece(true)));
                                });
                            }

                            for (Move move : possibleMoves) {
                                if (GridPane.getColumnIndex(node) == move.getX() && GridPane.getRowIndex(node) == move.getY()) {
                                    node.getStyleClass().add("tile-reversi-available");
                                }
                            }
                        }
                    }


                    tPlayerOne.setVisible(true);
                    tPlayerTwo.setVisible(false);

                } else if (match.getGameState() == GameState.OneWin) {
                    wcPlayerOne.setVisible(true);
                } else if (match.getGameState() == GameState.TwoWin) {
                    wcPlayerTwo.setVisible(true);
                } else if (match.getGameState() == GameState.Draw) {
                    // ???
                } else {
                    for (Node node : childNodes) {
                        if (node instanceof HBox) {
                            node.getStyleClass().add("tile-reversi-disabled");
                            node.getStyleClass().remove("tile-reversi-available");
                        }
                    }

                    tPlayerOne.setVisible(false);
                    tPlayerTwo.setVisible(true);
                }
            }
        }
    }

    private void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());

        if (field.getStyleClass().contains("tile-reversi-available")) {
            Move move = new Move(GameState.TurnOne, GridPane.getColumnIndex(field), GridPane.getRowIndex(field));
            localPlayer.putMove(move);
        }
    }
}
