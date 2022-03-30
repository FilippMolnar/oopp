package client.controllers;

import client.utils.ServerUtils;
import commons.Player;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.List;

public class HomeScreenMultiplayerCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;


    @FXML
    private TextField nameString;

    @FXML
    private Label labelErrors;

    @FXML
    private TextField serverField;
    @FXML
    private ComboBox<String> serversDropdown;


    @Inject
    HomeScreenMultiplayerCtrl(ServerUtils serverUtils, MainAppController appController) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void enterRoom() throws InterruptedException {
        appController.initializeScore();
        appController.setGameMode(true);
        String name = nameString.getText();
        String finalName = name.substring(0, Math.min(name.length(), 16));

        if (name.isEmpty()) {
            labelErrors.setText("Please enter your name");
            return;
        }

        if (!name.equals(finalName)) {
            // Send message to player that their name was too long
            labelErrors.setText("Your name was too long, we limited the number of characters");
        }
        serverUtils.initializeServer(serverField.getText());
        // Get request for the players that are currently waiting
        List<Player> playersInWaitingRoom = serverUtils.getAllNamesInWaitingRoom();
        if (!playersInWaitingRoom.isEmpty()) {
            // Check if the player has a unique name
            for (Player player : playersInWaitingRoom) {
                if (player.getName().equals(finalName)) { // Another player with this name was found
                    labelErrors.setText("This name is already taken, please try another");
                    return;
                }
            }
        }
        labelErrors.setText(""); // reset the error label
                // Show single player. 0 would be single player.
        this.appController.showNext();
        //this.serverUtils.postName(finalName);
        this.appController.setName(finalName);
        this.serverUtils.sendThroughSocket("/app/enterRoom", new Player(finalName));
    }

    public void backToHomeScreen() {
        appController.showHomeScreen();
    }


}
