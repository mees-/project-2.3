package ui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

public class Home extends BorderPane {
    public Home() {
        pane();
    }

    private void pane() {
        BorderPane pane = new BorderPane();
        pane.prefWidthProperty().bind(this.widthProperty().divide(3));

        Line line = new Line();
        line.setStartX(0);
        line.setStartY(20);
        line.setEndY(20);
        line.endXProperty().bind(pane.widthProperty().subtract(40));

        VBox left = new VBox();
        VBox center = new VBox();
        VBox right = new VBox();

        Label type = new Label("Gametype");
        Label players = new Label("Players");
        Label onlineOptions = new Label("Online Options");

        left.setAlignment(Pos.CENTER);
        center.setAlignment(Pos.CENTER);
        right.setAlignment(Pos.CENTER);

        type.getStyleClass().add("font-style");
        players.getStyleClass().add("font-style");
        onlineOptions.getStyleClass().add("font-style");

        Button local = new Button("Local");
        Button online = new Button("Online");

        left.getChildren().addAll(type, local, online);
        VBox.setMargin(local, new Insets(5,0,5,0));
        VBox.setMargin(online, new Insets(5,0,5,0));

        center.getChildren().addAll(players);
        right.getChildren().addAll(onlineOptions);

        pane.setLeft(left);
        pane.getLeft().setId("leftColor");
        BorderPane.setMargin(left, new Insets(10, 0, 0,20));

        pane.setCenter(center);
        pane.getCenter().setId("centerColor");
        BorderPane.setMargin(center, new Insets(10, 0, 0,0));

        pane.setRight(right);
        pane.getRight().setId("rightColor");
        BorderPane.setMargin(right, new Insets(10, 20, 0,0));

        pane.setTop(line);
        pane.getTop().prefHeight(50);
        BorderPane.setMargin(line, new Insets(10, 0, 0,20));

        this.setCenter(pane);

        Button b = new Button("Opvulling");
        b.prefWidthProperty().bind(this.widthProperty());
        b.setPrefHeight(200);
        this.setTop(b);
    }
}
