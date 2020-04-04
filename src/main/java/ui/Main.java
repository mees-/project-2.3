package ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ui.bottom.Bottom;

import java.io.File;

public class Main extends Application {
    private static BorderPane pane = new BorderPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pane.setCenter(new Home());
        pane.setBottom(new Bottom());

        Scene scene = new Scene(pane, 1000, 600);
        scene.getStylesheets().add("file:///" + new File("src/Styles/stylesheet.css").getAbsolutePath().replace("\\", "/"));
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css"); //(3)

        primaryStage.setTitle("Tha Koel GameBox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void changePane(int i) {
        switch (i) {
            case 1:
//                pane.setCenter(new Local());
                break;
            case 2:
//                pane.setCenter(new Online());
                break;
            default:
                pane.setCenter(new Home());
        }
    }
}
