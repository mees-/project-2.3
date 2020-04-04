package ui.home.center;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public abstract class Parent extends VBox {
    public Parent() {

    }

    public Line createLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(20);
        line.setEndY(20);
        line.endXProperty().bind(widthProperty().subtract(20));

        return line;
    }

    abstract void settings();
}
