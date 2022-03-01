package client.controllers;

import client.utils.ServerUtils;
import commons.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.List;

public class EnterNameCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TextField nameString;

    @FXML
    private Label labelErrors;

    @Inject
    EnterNameCtrl(ServerUtils serverUtils,MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void enterRoom(){
        labelErrors.setText("");
        String name = nameString.getText();
        String finalName = name.substring(0,15);
        if (name != finalName) {
            // Send message to player that their name was too long
            labelErrors.setText("Your name was too long, we limited the number of characters");
        }
        // Get request for the players that are currently waiting
        List<Player> playersInWaitingRoom = serverUtils.getAllNamesInWaitingRoom();
        if (!playersInWaitingRoom.isEmpty()) {
            // Check if the player has a unique name
            boolean flag = true;
            for (int i = 0; i < playersInWaitingRoom.size(); i++) {
                if (playersInWaitingRoom.get(i).getName().equals(finalName)) { // Another player with this name was found
                    labelErrors.setText("This name is already taken, please try another");
                    flag = false;
                }
            }
            if (flag) {
                this.appController.enterWaitingRoom(finalName);
                this.serverUtils.postName(finalName);
            }
        }
        else { // If the waiting room is empty, the player will for sure have a unique name
            this.appController.enterWaitingRoom(finalName);
            this.serverUtils.postName(finalName);
        }
    }
}
