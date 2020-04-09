package ui.home.center;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import ui.Home_oud;
import ui.settings.GameType;

public class Left extends Parent {
    private BorderPane pane;
    private Node rightNode;
    private VBox vbPlayerTwo, vbOptions;

    public Left(BorderPane pane) {
        this.pane = pane;
        rightNode = this.pane.getRight();
        this.pane.setRight(null);
        VBox vbCenter = (VBox) this.pane.getCenter();
        vbOptions = (VBox) vbCenter.getChildren().get(1);
        vbPlayerTwo = (VBox) vbOptions.getChildren().remove(1);

        settings();
    }

    @Override
    void settings() {
        Label type = new Label("Gametype");
        type.getStyleClass().add("font-style");
        setAlignment(Pos.CENTER);

        Button btnLocal = new Button("Local");
        Button btnOnline = new Button("Online");

        btnLocal.setId("local");
        btnOnline.setId("online");

        btnLocal.getStyleClass().addAll("btn-default", "btn-sm");
        btnOnline.getStyleClass().addAll("btn-default", "btn-sm");

        btnLocal.setOnAction(new ButtonType(GameType.LOCAL, btnOnline));
        btnOnline.setOnAction(new ButtonType(GameType.ONLINE, btnLocal));

        getChildren().addAll(type, btnLocal, btnOnline);

        VBox.setMargin(btnLocal, new Insets(5,0,5,0));
        VBox.setMargin(btnOnline, new Insets(5,0,5,0));
    }

    public class ButtonType implements EventHandler<ActionEvent> {
        private GameType type;
        private Button btn;

        public ButtonType(GameType type, Button btn) {
            this.type = type;
            this.btn = btn;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            Home_oud.gameType = type;
            settings((Button)actionEvent.getSource(), btn);
        }

        private void settings(Button btnOne, Button btnTwo) {
            Platform.runLater(
                    () -> {
                        if (btnOne.getStyleClass().contains("btn-sm")) {
                            btnOne.getStyleClass().add("btn-lg");
                            btnOne.getStyleClass().remove("btn-sm");
                        }
                        if (btnTwo.getStyleClass().contains("btn-lg")) {
                            btnTwo.getStyleClass().remove("btn-lg");
                            btnTwo.getStyleClass().add("btn-sm");
                        }

                        switch (type) {
                            case ONLINE:
                                pane.setRight(rightNode);

                                if (vbPlayerTwo != null) {
                                    vbOptions.getChildren().add(vbPlayerTwo);
                                    vbPlayerTwo = null;
                                }
                                break;
                            case LOCAL:
                                pane.setRight(null);

                                if (vbPlayerTwo == null) {
                                    vbPlayerTwo = (VBox) vbOptions.getChildren().remove(1);
                                }
                                break;
                            default:
                                System.out.println("Something went wrong?");
                        }
                    }
            );
        }
    }
}
