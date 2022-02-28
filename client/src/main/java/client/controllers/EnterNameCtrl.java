package client.controllers;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EnterNameCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TextField nameString;

    @Inject
    EnterNameCtrl(ServerUtils serverUtils,MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;

    }

    public void enterRoom(){
        String name = nameString.getText();
        this.appController.enterWaitingRoom(name);
        this.serverUtils.postName(name);
        // this.serverUtils.send("/app/waitingRoom", new Player(name));
        // this line of code was used to send the data through the socket connection
    }
}
