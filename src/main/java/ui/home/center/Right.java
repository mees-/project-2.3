package ui.home.center;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.Home;
import ui.settings.OnlineOption;

public class Right extends Parent {
    public Right() {
        settings();
    }

    @Override
    void settings() {
        setAlignment(Pos.CENTER);
        Label onlineOptions = new Label("Online Options");
        onlineOptions.getStyleClass().add("font-style");

        Button btnSubscribe = new Button("Subscribe");
        Button btnChallenge = new Button("Challenge");

        btnSubscribe.getStyleClass().addAll("btn-default", "btn-sm");
        btnChallenge.getStyleClass().addAll("btn-default", "btn-sm");

        btnSubscribe.setOnAction(new ButtonOption(OnlineOption.SUBSCRIBE, btnChallenge));
        btnChallenge.setOnAction(new ButtonOption(OnlineOption.CHALLENGE, btnSubscribe));

        getChildren().addAll(onlineOptions, btnSubscribe, btnChallenge);

        VBox.setMargin(btnSubscribe, new Insets(5,0,5,0));
        VBox.setMargin(btnChallenge, new Insets(5,0,5,0));
    }

    public static class ButtonOption implements EventHandler<ActionEvent> {
        private OnlineOption option;
        private Button btn;

        public ButtonOption(OnlineOption option, Button btn) {
            this.option = option;
            this.btn = btn;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            Home.option = option;
            settings((Button) actionEvent.getSource(), btn);
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
                    }
            );
        }
    }
}
