package ui.bottom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Bottom extends BorderPane {
    public Bottom() {
        settings();
    }

    private void settings() {
        // Create labels
        Label group = new Label("Groep D3");
        Label madeBy = new Label("Made by: Ivo, Jeroen, Joeri, Mees");

        // Create new BorderPane and background color
        setId("bottom");

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
        setLeft(left);
        setRight(right);
    }
}
