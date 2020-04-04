package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FXmlControllerTest {
    @FXML
    private Button btnSubscribe;

    @FXML
    private Button btnChallenge;

    public FXmlControllerTest() {
        System.out.println("testtesttest");
    }

    private void initialize() {

    }

    public void btnChange(ActionEvent event) {
        Button btnSource = (Button) event.getSource();
        Button btnOne = btnChallenge, btnTwo = btnSubscribe;

        if (btnSource.getId().equals("btnSubscribe")) {
            btnOne = btnSubscribe;
            btnTwo = btnChallenge;
        }

        if (btnOne.getStyleClass().contains("btn-sm")) {
            btnOne.getStyleClass().add("btn-lg");
            btnOne.getStyleClass().remove("btn-sm");
        }
        if (btnTwo.getStyleClass().contains("btn-lg")) {
            btnTwo.getStyleClass().remove("btn-lg");
            btnTwo.getStyleClass().add("btn-sm");
        }
    }
}
