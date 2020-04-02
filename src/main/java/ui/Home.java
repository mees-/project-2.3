package ui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class Home extends BorderPane {
    public Home() {
        pane();
    }

    private void pane() {
        VBox verticalOptions = new VBox();
        HBox horizontalOptions = new HBox();


        String style = "-fx-font-weight: bold; -fx-font-size: 20; -fx-min-width: 20%";
        Label type = new Label("Gametype");
        Label players = new Label("Players");
        Label onlineOptions = new Label("Online Options");




//        HBox.setMargin(type, new Insets(20, 25, 20, 25));
//        HBox.setMargin(players, new Insets(20, 25, 20, 25));
//        HBox.setMargin(onlineOptions, new Insets(20, 25, 20, 25));

        type.setStyle(style);
        players.setStyle(style);
        onlineOptions.setStyle(style);

        horizontalOptions.getChildren().addAll(type, players, onlineOptions);

        verticalOptions.getChildren().addAll(horizontalOptions);

        this.setCenter(verticalOptions);
    }


}
