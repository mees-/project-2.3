package ui.controller;

import ai.Ai;
import framework.*;
import framework.player.*;
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
import tictactoe.TicTacToeBoard;
import ui.Main;
import ui.update.GameStateUpdate;

import static javafx.scene.paint.Color.BLACK;

public class TicTacToeController extends Controller {
    @FXML
    protected GridPane gpTicTacToe;

    private SVGPath crossHover = createCrossHover();
    private Circle circleHover = createCircleHover();

    public TicTacToeController(Main main, Match match) {
        super(main, match);
    }

    public void start() {
        run.start();
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
        setupNames();
        childNodes = gpTicTacToe.getChildren();

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
            if (match.getGameState() == GameState.TurnOne && match.getPlayers().one instanceof UIPlayer) {
                hBox.getChildren().add(crossHover);
            } else if (match.getGameState() == GameState.TurnTwo && match.getPlayers().two instanceof UIPlayer){
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

            if (!gameState.isEnd()) {
                if (gameState == GameState.TurnOne) {
                    currentPlayer = match.getPlayers().one;
                    tPlayerOne.setVisible(true);
                    tPlayerTwo.setVisible(false);
                } else {
                    currentPlayer = match.getPlayers().two;
                    tPlayerOne.setVisible(false);
                    tPlayerTwo.setVisible(true);
                }

                updateBoard(board);
            } else if (gameState == GameState.OneWin) {
                wcPlayerOne.setVisible(true);
            } else if (gameState == GameState.TwoWin) {
                wcPlayerTwo.setVisible(true);
            }

            if (gameState.isEnd()) {
                updateBoard(board);
                resetMatch();
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        main.changeToHome();
    }

    private void resetMatch() {
        match = null;
    }

    private void updateBoard(BoardInterface board) {
        for (Node node : childNodes) {
            if (node instanceof HBox) {
                if (currentPlayer instanceof UIPlayer || currentPlayer instanceof Ai || currentPlayer instanceof LocalConnectedPlayer) {
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

    private void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());
        System.out.println("click");

        Move move = new Move(currentPlayer.getTurn(), (GridPane.getColumnIndex(field)), (GridPane.getRowIndex(field)));
        ((BlockingPlayer)((HigherOrderPlayer) currentPlayer).getOriginal()).putMove(move);
    }
}
