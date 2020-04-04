package ui.home.top;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Top extends HBox {
    public Top() {
        settings();
    }

    public void settings() {
        Image image = null;
        try {
            image = new Image(new FileInputStream("path of the image"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);



    }
}
