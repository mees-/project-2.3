package ui.home;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.Home;
import ui.settings.OnlineOption;
import ui.settings.PlayerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Center extends Parent {
    private static HashMap<String, Integer> playerOneType, playerTwoType;
    static PlayerType playerOne, playerTwo;
    public VBox vbPlayerTwo;

    public Center() {
        playerOneType = new HashMap<>();
        playerTwoType = new HashMap<>();
        settings();
    }

    @Override
    void settings() {
        setAlignment(Pos.CENTER);
        Label players = new Label("Players");
        players.getStyleClass().add("font-style");

        Label lblPlayerOne = new Label("Player 1");
        TextField txtPlayerOne = new TextField("");
        HBox hboxPlayerOneOptions = new HBox();

        Label lblPlayerTwo = new Label("Player 2");
        TextField txtPlayerTwo = new TextField("");
        HBox hboxPlayerTwoOptions = new HBox();

        hboxPlayerOneOptions.setAlignment(Pos.CENTER);
        hboxPlayerTwoOptions.setAlignment(Pos.CENTER);

        ComboBox<String> cbPlayerOneType = new ComboBox<>();
        cbPlayerOneType.valueProperty().addListener(new ComboBoxPlayerOneType());
        cbPlayerOneType.setItems(FXCollections.observableArrayList(createComboBoxPlayerOne()));

        ComboBox<String> cbPlayerTwoType = new ComboBox<>();
        cbPlayerTwoType.valueProperty().addListener(new ComboBoxPlayerTwoType());
        cbPlayerTwoType.setItems(FXCollections.observableArrayList(createComboBoxPlayerTwo()));

        txtPlayerOne.maxWidthProperty().bind(widthProperty().divide(2));
        VBox.setMargin(txtPlayerOne, new Insets(0,10,0,10));
        hboxPlayerOneOptions.getChildren().addAll(txtPlayerOne, cbPlayerOneType);

        txtPlayerTwo.maxWidthProperty().bind(widthProperty().divide(2));
        VBox.setMargin(txtPlayerTwo, new Insets(0,10,0,10));
        hboxPlayerTwoOptions.getChildren().addAll(txtPlayerTwo, cbPlayerTwoType);

        VBox playerOptions = new VBox();
        playerOptions.setAlignment(Pos.CENTER);

        VBox vbPlayerOne = new VBox();
        vbPlayerOne.getChildren().addAll(lblPlayerOne,hboxPlayerOneOptions);

        vbPlayerTwo = new VBox();
        vbPlayerTwo.getChildren().addAll(lblPlayerTwo,hboxPlayerTwoOptions);

        playerOptions.getChildren().addAll(vbPlayerOne, vbPlayerTwo);

        getChildren().addAll(players, playerOptions);
    }

    private ArrayList<String> createComboBoxPlayerOne() {
        playerOneType.put("AI OFF", 0);
        playerOneType.put("AI ON", 1);

        ArrayList<String> typeString = new ArrayList<>();
        for (Map.Entry<String, Integer> item : playerOneType.entrySet()) {
            typeString.add(item.getKey());
        }

        return typeString;
    }

    private ArrayList<String> createComboBoxPlayerTwo() {
        playerTwoType.put("AI ON", 1);
        playerTwoType.put("AI OFF", 0);

        ArrayList<String> typeString = new ArrayList<>();
        for (Map.Entry<String, Integer> item : playerTwoType.entrySet()) {
            typeString.add(item.getKey());
        }

        return typeString;
    }

    public static class ComboBoxPlayerOneType implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            switch (playerOneType.get(t1)) {
                case 0:
                    playerOne = PlayerType.HUMAN;
                    break;
                case 1:
                    playerOne = PlayerType.AI;
                    break;
                default:
                    System.out.println("Something went wrong!");
            }
        }
    }

    public static class ComboBoxPlayerTwoType implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            switch (playerTwoType.get(t1)) {
                case 0:
                    playerTwo = PlayerType.HUMAN;
                    break;
                case 1:
                    playerTwo = PlayerType.AI;
                    break;
                default:
                    System.out.println("Something went wrong!");
            }
        }
    }
}
