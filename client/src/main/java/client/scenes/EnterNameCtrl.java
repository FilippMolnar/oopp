package client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import client.utils.ServerUtils;

import javax.inject.Inject;

public class EnterNameCtrl {

    private final MainAppController appController;
    private final ServerUtils server;

    @FXML
    private TextField nameString;

    @Inject
    EnterNameCtrl(MainAppController appController, ServerUtils server){
        this.appController = appController;
        this.server = server;
    }

    public void enterRoom(){
        String name = nameString.getText();
        this.server.addName(name);
        this.appController.changeScene(name);
    }
}
