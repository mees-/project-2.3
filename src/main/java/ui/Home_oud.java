package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import ui.home.center.*;
import ui.settings.GameType;
import ui.settings.OnlineOption;
import ui.settings.PlayerType;

public class Home_oud extends BorderPane {
    public static BorderPane pane;
    public static GameType gameType;
    public static OnlineOption option;
    public static PlayerType playerType;

    public Home_oud() {
        pane = new BorderPane();
        pane.prefWidthProperty().bind(widthProperty().divide(3));

        settings();
    }

    private void settings() {
        VBox right = new Right();
        pane.setRight(right);
        pane.getRight().setId("rightColor");
        BorderPane.setMargin(right, new Insets(10, 20, 0,0));
        right.prefWidthProperty().bind(widthProperty().divide(3));

        VBox center = new Center();
        center.prefWidthProperty().bind(widthProperty().divide(3));
        pane.setCenter(center);
        pane.getCenter().setId("centerColor");
        BorderPane.setMargin(center, new Insets(10, 0, 0,0));

        VBox left = new Left(pane);
        left.prefWidthProperty().bind(widthProperty().divide(3));
        pane.setLeft(left);
        pane.getLeft().setId("leftColor");
        BorderPane.setMargin(left, new Insets(10, 0, 0,20));

        VBox top = new Top();
        pane.setTop(top);
        pane.getTop().prefHeight(50);
        BorderPane.setMargin(top, new Insets(10, 0, 0,20));

        VBox bottom = new Bottom();
        pane.setBottom(bottom);
        pane.getBottom().prefHeight(50);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        BorderPane.setMargin(bottom, new Insets(10, 10, 10, 10));

        setCenter(pane);

        // Opvulling
        Button b = new Button("Opvulling");
        b.prefWidthProperty().bind(this.widthProperty());
        b.setPrefHeight(200);
        setTop(b);
    }
}
