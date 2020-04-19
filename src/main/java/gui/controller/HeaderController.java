package gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class HeaderController {

    @FXML
    private Text headerTitle;

    @FXML
    private Text headerSubTitle;

    public void setup(String title, String subTitle) {
        this.headerTitle.setText(title);
        this.headerSubTitle.setText(subTitle);
    }
}
