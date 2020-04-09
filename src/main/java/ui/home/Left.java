package ui.home;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import ui.Home;
import ui.settings.GameType;

public class Left extends Parent {
    private static Button btnLocal, btnOnline;
    private BorderPane pane;
    private Node rightNode;
    private VBox right;

    public Left(BorderPane pane) {
        this.pane = pane;
        rightNode = this.pane.getRight();
        this.pane.setRight(null);
        settings();
    }

    @Override
    void settings() {
        Label type = new Label("Gametype");
        type.getStyleClass().add("font-style");
        setAlignment(Pos.CENTER);

        btnLocal = new Button("Local");
        btnOnline = new Button("Online");

        btnLocal.getStyleClass().addAll("btn-default", "btn-sm");
        btnOnline.getStyleClass().addAll("btn-default", "btn-sm");

        btnLocal.setOnAction(new ButtonLocal());
        btnOnline.setOnAction(new ButtonOnline());

        getChildren().addAll(type, btnLocal, btnOnline);

        VBox.setMargin(btnLocal, new Insets(5,0,5,0));
        VBox.setMargin(btnOnline, new Insets(5,0,5,0));
    }

    public class ButtonLocal implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            Home.type = GameType.LOCAL;

            Platform.runLater(
                    () -> {
                        if (btnLocal.getStyleClass().contains("btn-sm")) {
                            btnLocal.getStyleClass().remove("btn-sm");
                            btnLocal.getStyleClass().add("btn-lg");
                        }
                        if (btnOnline.getStyleClass().contains("btn-lg")) {
                            btnOnline.getStyleClass().remove("btn-lg");
                            btnOnline.getStyleClass().add("btn-sm");
                        }
                        if (pane.getRight() != null) {
                            rightNode = pane.getRight();
                        }
                        pane.setRight(null);
//                        rightNode.
                    }
            );
        }
    }

    public class ButtonOnline implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            Home.type = GameType.ONLINE;

            Platform.runLater(
                    () -> {
                        if (btnOnline.getStyleClass().contains("btn-sm")) {
                            btnOnline.getStyleClass().add("btn-lg");
                            btnOnline.getStyleClass().remove("btn-sm");
                        }
                        if (btnLocal.getStyleClass().contains("btn-lg")) {
                            btnLocal.getStyleClass().remove("btn-lg");
                            btnLocal.getStyleClass().add("btn-sm");
                        }
                        pane.setRight(rightNode);
                    }
            );
        }
    }
}
