package client.controllers;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class HomeScreenSingleplayerCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TextField nameString;

    @FXML
    private Label labelErrors;

    @Inject
    HomeScreenSingleplayerCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void enterRoom(){
        String name = nameString.getText();
        System.out.println(name);
        String finalName = name.substring(0,Math.min(name.length(),16));
        if (!name.equals(finalName)) {
            // Send message to player that their name was too long
            labelErrors.setText("Your name was too long, we limited the number of characters");
        }

        // Show single player. 1 would be multiplayer.
        this.appController.showNext(0);
        this.serverUtils.postName(finalName);
    }
}
