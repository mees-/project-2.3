package ui.home;

import javafx.scene.shape.Line;

public class Top extends Parent {
    public Top() {
       settings();
    }

    @Override
    void settings() {
        Line line = createLine();

        getChildren().addAll(line);
    }


}
