package ui.controller;

import javafx.fxml.FXML;

public class HeaderController {

    @FXML
    private String title;

    @FXML
    private String subTitle;

    public HeaderController() {
    }

    public void setup(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }
}
