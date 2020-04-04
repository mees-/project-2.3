package ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Main extends Application {
    private static BorderPane pane = new BorderPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/templates/template.fxml"));

//        pane.setCenter(new Home());
//        pane.setBottom(new Bottom());

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add("/css/stylesheet.css");
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
