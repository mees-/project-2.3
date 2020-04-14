package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;

public class Root extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Tha Koel Gamebox");
        //stage.getIcons().add(new Image("/img/tictactoe.png"));
        initUI(stage);
        stage.show();
    }

    private void initUI(Stage stage) throws IOException {
        VBox root = FXMLLoader.load(getClass().getResource("/view/root.fxml"));

        Pane header = (Pane) FXMLLoader.load(getClass().getResource("/view/components/header.fxml"));
        Pane footer = (Pane) FXMLLoader.load(getClass().getResource("/view/components/footer.fxml"));

        Pane home = (Pane) FXMLLoader.load(getClass().getResource("/view/lobby.fxml"));

        root.getChildren().add(header);
        root.getChildren().add(home);
        root.getChildren().add(footer);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

}
