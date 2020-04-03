package ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    BorderPane pane = new BorderPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pane.setCenter(new Home());
        pane.setBottom(createBottom());

        Scene scene = new Scene(pane, 1000, 600);
        scene.getStylesheets().add("file:///" + new File("src/Styles/stylesheet.css").getAbsolutePath().replace("\\", "/"));

        primaryStage.setTitle("Tha Koel GameBox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane createBottom() {
        // Create labels
        Label group = new Label("Groep D3");
        Label madeBy = new Label("Ivo, Jeroen, Joeri, Mees");

        // Create new BorderPane and background color
        BorderPane bottomPane = new BorderPane();
        bottomPane.setId("id");

        // Create two HBox's
        HBox left = new HBox();
        HBox right = new HBox();

        // Styling the HBox
        HBox.setMargin(group, new Insets(3, 6, 3, 0));
        HBox.setMargin(madeBy, new Insets(3, 0, 3, 6));

        left.setAlignment(Pos.BOTTOM_LEFT);
        right.setAlignment(Pos.BOTTOM_RIGHT);

        // Setting the new nodes on the HBox's
        left.getChildren().add(madeBy);
        right.getChildren().add(group);

        // Set the HBox's on the new pane
        bottomPane.setLeft(left);
        bottomPane.setRight(right);

        return bottomPane;
    }
}
