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
        Pane reversi = FXMLLoader.load(getClass().getResource("/view/reversi.fxml"));
        root.getChildren().add(reversi);

        ObservableList<Node> workingCollection = FXCollections.observableArrayList(root.getChildren());
        Collections.swap(workingCollection,0,1);
        root.getChildren().setAll(workingCollection);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

}
