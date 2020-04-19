package gui.controller;

import ai.Ai;
import framework.*;
import framework.player.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import tictactoe.TicTacToeBoard;
import gui.Main;
import gui.update.GameStateUpdate;

import static javafx.scene.paint.Color.BLACK;

public class TicTacToeController extends GameController {

    private SVGPath crossHover = createCrossHover();
    private Circle circleHover = createCircleHover();

    public TicTacToeController(Main main) {
        super(main);
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

    private Circle createCircleHover() {
        Circle circle = createCircle();
        circle.getStyleClass().add("piece-tictactoe-possible-move");

        return circle;
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

    protected void setupNames() {
        Platform.runLater(() -> {
            new Thread(() -> {
                players = match.getPlayers();
                txtPlayerOne.setText(players.one.getUsername());
                txtPlayerTwo.setText(players.two.getUsername());
            }).start();
        });
    }

    public void setup(Match match) {
        super.setup(match);
        setupNames();
        childNodes = gpGame.getChildren();

        for (Node node : childNodes) {
            if (node instanceof HBox) {
                node.getStyleClass().add("tile-tictactoe-disabled");
                node.setOnMouseClicked(this::mouseClick);
                node.setOnMouseEntered(this::mouseHoverIn);
                node.setOnMouseExited(this::mouseHoverOut);
            }
        }
    }

    private void mouseHoverIn(Event event) {
        HBox hBox = (HBox) event.getSource();
        if (hBox.getChildren().size() == 0) {
            if (match.getGameState() == GameState.TurnOne && match.getPlayers().one.isComposedOf(UIPlayer.class)) {
                hBox.getChildren().add(crossHover);
            } else if (match.getGameState() == GameState.TurnTwo && match.getPlayers().two.isComposedOf(UIPlayer.class)){
                hBox.getChildren().add(circleHover);
            }
        }
    }

    private void mouseHoverOut(MouseEvent event) {
        HBox hBox = (HBox) event.getSource();
        hBox.getChildren().remove(crossHover);
        hBox.getChildren().remove(circleHover);
    }

    public void run() {
        while (match != null) {
            GameStateUpdate update;

            try {
                update = match.getGameUpdate();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            TicTacToeBoard board = ((TicTacToeBoard) update.getBoard());
            GameState gameState = update.getGameState();

            setTurns(gameState);

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
                    Platform.runLater(() -> node.getStyleClass().remove("tile-tictactoe-disabled"));
                } else if (!node.getStyleClass().contains("tile-tictactoe-disabled")) {
                    Platform.runLater(() -> node.getStyleClass().add("tile-tictactoe-disabled"));
                }

                CellContent content = board.getCell(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
                HBox hBox = (HBox) node;

                if (content == CellContent.Local) {
                    Platform.runLater( () -> {
                        if (hBox.getChildren().size() == 0 || (hBox.getChildren().size() == 1 && hBox.getChildren().contains(crossHover))) {
                            hBox.getChildren().remove(crossHover);
                            hBox.getChildren().add((createCross()));
                        }
                    });
                } else if (content == CellContent.Remote) {
                    Platform.runLater( () -> {
                        if (hBox.getChildren().size() == 0 || (hBox.getChildren().size() == 1 && hBox.getChildren().contains(circleHover))) {
                            hBox.getChildren().remove(circleHover);
                            hBox.getChildren().add((createCircle()));
                        }
                    });
                }
            }
        }
    }

    protected void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());

        Move move = new Move(match.getCurrentPlayer().getTurn(), (GridPane.getColumnIndex(field)), (GridPane.getRowIndex(field)));
        ((BlockingPlayer) match.getCurrentPlayer().getSource()).putMove(move);
    }
}
