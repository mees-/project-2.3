package ui.home;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.Home;
import ui.settings.GameType;
import ui.settings.OnlineOption;

public class Right extends Parent {
    private static Button btnSubscribe, btnChallenge;

    public Right() {
        settings();
    }

    @Override
    void settings() {
        setAlignment(Pos.CENTER);
        Label onlineOptions = new Label("Online Options");
        onlineOptions.getStyleClass().add("font-style");

        btnSubscribe = new Button("Subscribe");
        btnChallenge = new Button("Challenge");

        btnSubscribe.getStyleClass().addAll("btn-default", "btn-sm");
        btnChallenge.getStyleClass().addAll("btn-default", "btn-sm");

        btnSubscribe.setOnAction(new ButtonSubscribe());
        btnChallenge.setOnAction(new ButtonChallenge());

        getChildren().addAll(onlineOptions, btnSubscribe, btnChallenge);

        VBox.setMargin(btnSubscribe, new Insets(5,0,5,0));
        VBox.setMargin(btnChallenge, new Insets(5,0,5,0));
    }

    public static class ButtonSubscribe implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
//            Home.option = OnlineOption.SUBSCRIBE;

            Platform.runLater(
                    () -> {
                        if (btnSubscribe.getStyleClass().contains("btn-sm")) {
                            btnSubscribe.getStyleClass().remove("btn-sm");
                            btnSubscribe.getStyleClass().add("btn-lg");
                        }
                        if (btnChallenge.getStyleClass().contains("btn-lg")) {
                            btnChallenge.getStyleClass().remove("btn-lg");
                            btnChallenge.getStyleClass().add("btn-sm");
                        }
                    }
            );

        }
    }

    public static class ButtonChallenge implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
//            Home.option = OnlineOption.CHALLENGE;

            Platform.runLater(
                    () -> {
                        if (btnChallenge.getStyleClass().contains("btn-sm")) {
                            btnChallenge.getStyleClass().add("btn-lg");
                            btnChallenge.getStyleClass().remove("btn-sm");
                        }
                        if (btnSubscribe.getStyleClass().contains("btn-lg")) {
                            btnSubscribe.getStyleClass().remove("btn-lg");
                            btnSubscribe.getStyleClass().add("btn-sm");
                        }
                    }
            );
        }
    }
}
