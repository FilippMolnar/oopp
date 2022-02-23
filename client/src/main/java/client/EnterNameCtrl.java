package client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EnterNameCtrl {

    private final MainAppController appController;

    @FXML
    private TextField nameString;

    @Inject
    EnterNameCtrl(MainAppController appController){
        this.appController = appController;
    }

    public void enterRoom(){
        this.appController.changeScene(nameString.getText());
    }
}
