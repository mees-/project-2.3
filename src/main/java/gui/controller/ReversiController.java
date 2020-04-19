package gui.controller;

import ai.Ai;
import framework.*;
import framework.player.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import reversi.ReversiBoard;
import gui.Main;
import gui.update.GameStateUpdate;

import static javafx.scene.paint.Color.BLACK;

public class ReversiController extends GameController {
    public ReversiController(Main main) {
        super(main);
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
                Player current = match.getCurrentPlayer();
                if (current == players.one) {
                    tPlayerOne.setVisible(true);
                    tPlayerTwo.setVisible(false);
                } else {
                    tPlayerTwo.setVisible(true);
                    tPlayerOne.setVisible(false);
                }
            }).start();
        });
    }

    public void setup(Match match) {
        super.setup(match);
        setupNames();
        childNodes = gpGame.getChildren();

        for (Node node : childNodes) {
            if (node instanceof HBox) {
                if (!node.getStyleClass().contains("tile-reversi-disabled")) {
                    node.getStyleClass().add("tile-reversi-disabled");
                }

                node.setOnMouseClicked((this::mouseClick));
            }
        }
    }

    protected void run() {
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

            setTurns(gameState);

            Platform.runLater( () -> {
                sPlayerTwo.setText(Integer.toString(score[1]));
                sPlayerOne.setText(Integer.toString(score[0]));
            });

            updateBoard(board, gameState);

            if (gameState.isEnd()) {
                isEnd(gameState);
                break;
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        main.changePane();
    }

    protected void updateBoard(BoardInterface board, GameState gameState) {
        for (Node node : childNodes) {
            if (node instanceof HBox) {
                ComposablePlayer current = match.getCurrentPlayer();
                if (current.isComposedOf(UIPlayer.class) || current.getSource() instanceof Ai) {
                    Platform.runLater(() -> node.getStyleClass().remove("tile-reversi-disabled"));
                } else if (!node.getStyleClass().contains("tile-reversi-disabled")) {
                    Platform.runLater(() -> node.getStyleClass().add("tile-reversi-disabled"));
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
                if (match.getCurrentPlayer().isComposedOf(UIPlayer.class)) {
                    for (Move move : board.getValidMoves(gameState)) {
                        if ((GridPane.getColumnIndex(node) - 1) == move.getX() && (GridPane.getRowIndex(node) - 1) == move.getY()) {
                            Platform.runLater(() -> node.getStyleClass().add("tile-reversi-available"));
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

    protected void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());

        if (field.getStyleClass().contains("tile-reversi-available")) {
            Move move = new Move(match.getCurrentPlayer().getTurn(), (GridPane.getColumnIndex(field) - 1), (GridPane.getRowIndex(field) - 1));
            ((BlockingPlayer)match.getCurrentPlayer().getSource()).putMove(move);
        }
    }
}
