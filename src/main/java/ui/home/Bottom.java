package ui.home;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class Bottom extends Parent {
    public Bottom() {
        settings();
    }

    @Override
    void settings() {
        Line line = createLine();

        Button buttonPlay = new Button("Play");
        buttonPlay.getStyleClass().add("btn-default");

        VBox.setMargin(line, new Insets(10, 0, 10,0));
        VBox.setMargin(buttonPlay, new Insets(10, 0, 10,0));

        getChildren().addAll(line, buttonPlay);
    }
}
