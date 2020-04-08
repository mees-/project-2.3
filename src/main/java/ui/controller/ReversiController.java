package ui.controller;

import framework.*;
import framework.player.LocalPlayer;
import framework.player.Player;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import framework.player.Players;
import ui.Main;

import java.util.Set;

import static javafx.scene.paint.Color.BLACK;

public class ReversiController {
    private Framework framework;
    private Main main;
    private Player localPlayer;
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


    public ReversiController(Main main, Framework framework) {
        this.framework = framework;
        this.main = main;
        localPlayer = this.framework.getLocalPlayer();

        match = framework.getMatch();
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

    public void setup() {
        childNodes = gpReversi.getChildren();

        Players players = match.getPlayers();

//        txtPlayerOne.setText(players.one.getUsername());
//        txtPlayerTwo.setText(players.one.getUsername());

        for (Node node : childNodes) {
            if (node instanceof HBox) {
                node.getStyleClass().add("tile-reversi-disabled");
                node.setOnMouseClicked((this::mouseClick));

                if ((GridPane.getColumnIndex(node) == 4 && GridPane.getRowIndex(node) == 4) || (GridPane.getColumnIndex(node) == 5 && GridPane.getRowIndex(node) == 5)) {
                    ((HBox) node).getChildren().add(setupPiece(false));
                }

                if ((GridPane.getColumnIndex(node) == 4 && GridPane.getRowIndex(node) == 5) || (GridPane.getColumnIndex(node) == 5 && GridPane.getRowIndex(node) == 4)) {
                    ((HBox) node).getChildren().add(setupPiece(true));
                }
            }
        }
    }

    public void run() {
        while(true) {
            if (localPlayer.getTurn() == GameState.TurnOne) {
                BoardInterface board = match.getGame().getBoard();
                Set<Move> possibleMoves = board.getValidMoves(GameState.TurnOne);

                for (Node node : childNodes) {
                    if (node instanceof HBox) {
                        node.getStyleClass().remove("tile-reversi-disabled");
                        CellContent content = board.getCell(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));

                        if (content == CellContent.Local) {
                            ((HBox) node).getChildren().add(setupPiece(false));
                        } else if (content == CellContent.Remote) {
                            ((HBox) node).getChildren().add(setupPiece(true));
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
            } else if(localPlayer.getTurn() == GameState.OneWin) {
                wcPlayerOne.setVisible(true);
            } else if(localPlayer.getTurn() == GameState.TwoWin) {
                wcPlayerTwo.setVisible(true);
            } else if(localPlayer.getTurn() == GameState.Draw) {
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

    private void mouseClick(MouseEvent event) {
        HBox field = ((HBox)event.getSource());

        if (field.getStyleClass().contains("tile-reversi-available")) {
            Move move = new Move(GameState.TurnOne, GridPane.getColumnIndex(field), GridPane.getRowIndex(field));
            ((LocalPlayer) localPlayer).putMove(move);
        }
    }
}
