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

    private boolean test = false;

    public ReversiController(Main main, Framework framework, LocalPlayer localPlayer) {
        this.framework = framework;
        this.main = main;
        this.localPlayer = localPlayer;
        System.out.println(localPlayer.getUsername());
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
            if (test) {
                test = false;
                System.out.println("click");
            }

            if (board != match.getGame().getBoard()) {
                System.out.println(match.getGameState());

                if (match.getGameState() == GameState.TurnOne || match.getGameState() == GameState.TurnTwo) {
                    board = match.getGame().getBoard();

                    updateBoard(board, match.getGameState());

                    if (match.getGameState() == GameState.TurnOne) {
                        tPlayerOne.setVisible(true);
                        tPlayerTwo.setVisible(false);
                    } else if (match.getGameState() == GameState.TurnTwo) {
                        tPlayerOne.setVisible(false);
                        tPlayerTwo.setVisible(true);
                    }
                } else if (match.getGameState() == GameState.OneWin) {
                    wcPlayerOne.setVisible(true);
                } else if (match.getGameState() == GameState.TwoWin) {
                    wcPlayerTwo.setVisible(true);
                } else if (match.getGameState() == GameState.Draw) {
                    // ???
                } else {
//                    for (Node node : childNodes) {
//                        if (node instanceof HBox) {
//                            if (!node.getStyleClass().contains("tile-reversi-disabled")) {
//                                node.getStyleClass().add("tile-reversi-disabled");
//                            }
//                            node.getStyleClass().remove("tile-reversi-available");
//                        }
//                    }
//                    updateBoard(board, match.getGameState());


                }
            }
        }
    }

    private void updateBoard(BoardInterface board, GameState turn) {
        Set<Move> possibleMoves = board.getValidMoves(GameState.TurnOne);

        for (Node node : childNodes) {
            if (node instanceof HBox) {
                if (turn == GameState.TurnOne) {
                    node.getStyleClass().remove("tile-reversi-disabled");
                } else if (turn == GameState.TurnTwo) {
                    if (!node.getStyleClass().contains("tile-reversi-disabled")) {
                        node.getStyleClass().add("tile-reversi-disabled");
                    }
                    node.getStyleClass().remove("tile-reversi-available");
                }

                CellContent content = board.getCell(GridPane.getColumnIndex(node) - 1, GridPane.getRowIndex(node) - 1);
                HBox hBox = (HBox) node;

                if (content == CellContent.Local) {
                    changeHbox(hBox, false);
                    Platform.runLater(() -> {
                        hBox.getChildren().add((setupPiece(false)));
                    });
                } else if (content == CellContent.Remote) {
                    changeHbox(hBox, true);
                    Platform.runLater(() -> {
                        hBox.getChildren().add((setupPiece(true)));
                    });
                }

                if (turn == GameState.TurnOne) {
                    for (Move move : possibleMoves) {
                        if ((GridPane.getColumnIndex(node) - 1) == move.getX() && (GridPane.getRowIndex(node) - 1) == move.getY()) {
                            node.getStyleClass().add("tile-reversi-available");
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
            if (!hBox.getChildren().get(0).getStyleClass().contains(cssClass) && (hBox.getChildren().get(0) instanceof Circle)) {
                if (hBox.getChildren().get(0).getStyleClass().contains(cssClassContains)) {
                    hBox.getChildren().remove(0);
                }
            }
        }
    }

    private void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());

        if (field.getStyleClass().contains("tile-reversi-available")) {
            Move move = new Move(GameState.TurnOne, (GridPane.getColumnIndex(field) - 1), (GridPane.getRowIndex(field) - 1));
            localPlayer.putMove(move);
            test = true;
//            match.setGameState(GameState.TurnTwo);
        }
    }
}
