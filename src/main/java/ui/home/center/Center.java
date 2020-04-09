package ui.home.center;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.Home_oud;
import ui.settings.PlayerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Center extends Parent {
    public Center() {
        settings();
    }

    @Override
    void settings() {
        setAlignment(Pos.CENTER);
        Label players = new Label("Players");
        players.getStyleClass().add("font-style");

        VBox playerOptions = new VBox();
        playerOptions.setAlignment(Pos.CENTER);
        playerOptions.setId("playerOptions");

        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("AI ON", 1);
        hashMap.put("AI OFF", 0);

        playerOptions.getChildren().addAll(createPlayerOptions(hashMap, "Player 1"), createPlayerOptions(hashMap, "Player 2"));

        getChildren().addAll(players, playerOptions);
    }

    private VBox createPlayerOptions(HashMap<String, Integer> hashMap, String str) {
        VBox vbPlayer = new VBox();
        vbPlayer.setAlignment(Pos.CENTER);
        Label lblPlayer = new Label(str);
        lblPlayer.setPadding(new Insets(5, 0,3, 0));

        TextField txtPlayer = new TextField();
        HBox hboxPlayerOptions = new HBox();
        hboxPlayerOptions.setAlignment(Pos.CENTER);

        ComboBox<String> cbPlayerType = new ComboBox<>();
        cbPlayerType.valueProperty().addListener(new ComboBoxPlayerType(hashMap));
        cbPlayerType.setItems(FXCollections.observableArrayList(createComboBox(hashMap)));

        txtPlayer.maxWidthProperty().bind(widthProperty().divide(2));
        VBox.setMargin(txtPlayer, new Insets(0,10,0,10));
        hboxPlayerOptions.getChildren().addAll(txtPlayer, cbPlayerType);

        vbPlayer.getChildren().addAll(lblPlayer, hboxPlayerOptions);

        return vbPlayer;
    }

    private ArrayList<String> createComboBox(HashMap<String, Integer> hashMap) {
        ArrayList<String> typeString = new ArrayList<>();
        for (Map.Entry<String, Integer> item : hashMap.entrySet()) {
            typeString.add(item.getKey());
        }

        return typeString;
    }

    public static class ComboBoxPlayerType implements ChangeListener<String> {
        HashMap<String, Integer> hashMap;

        public ComboBoxPlayerType(HashMap<String, Integer> hashMap) {
            this.hashMap = hashMap;
        }

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            switch (hashMap.get(t1)) {
                case 0:
                    Home_oud.playerType = PlayerType.HUMAN;
                    break;
                case 1:
                    Home_oud.playerType = PlayerType.AI;
                    break;
                default:
                    System.out.println("Something went wrong!");
            }
        }
    }
}
