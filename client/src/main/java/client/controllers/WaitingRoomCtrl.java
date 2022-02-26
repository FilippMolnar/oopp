package client.controllers;

import client.utils.ServerUtils;
import commons.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingRoomCtrl implements Initializable {

    private final MainAppController appController;
    private final ServerUtils serverUtils;
    private List<Player> playerList;

    @FXML
    private AnchorPane pane;

    @FXML
    private Label morePlayersWaitingRoomLabel;
    private int otherPlayersWaitingRoom = 0;


    @Inject
    WaitingRoomCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    /**
     * function called whenever the playerlist changes
     */
    public void handleNameAdded() {
        displayPlayer(playerList.size() - 1);
    }

    /**
     * Sets the text field of newly added player
     * @param index the array index of the player
     */
    public void displayPlayer(int index) {
        // this Platform.runLater() is used to make sure that this code is run on the JAVAFX thread
        // if you don't add this it won't work :)
        Platform.runLater(() -> {
            try {
                String playerName = playerList.get(index).name;
                StackPane child = (StackPane) pane.getChildren().get(index);
                Label label = (Label) child.getChildren().get(1);
                System.out.println(playerName);
                label.setText(playerName);
                child.setVisible(true);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Running the label!");
                morePlayersWaitingRoomLabel.setVisible(true);
                otherPlayersWaitingRoom++;
                morePlayersWaitingRoomLabel.setText("and " + otherPlayersWaitingRoom + " more players");
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize called by the waiting roomCtrl");
        for (Node node : pane.getChildren()) {
            node.setVisible(false); // there are 6-7 circle added by default but I hide them
        }
        morePlayersWaitingRoomLabel.setVisible(false); // hide the label
        this.playerList = serverUtils.getAllNamesInWaitingRoom(); // get request on the players that are currently waiting
        for (int i = 0; i < this.playerList.size(); i++) {
            displayPlayer(i); // display them
        }
        this.serverUtils.registerForMessages("/topic/waitingRoom", Player.class, player -> {
            playerList.add(player);
            handleNameAdded();
        });
    }
}
