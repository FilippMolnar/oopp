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


    @Inject
    WaitingRoomCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }


    /**
      * Moves all players one to the right and adds one player up front.
      * @param toAdd the player to add at the front
      */
    public void movePlayers(Player toAdd) {
        // this Platform.runLater() is used to make sure that this code is run on the JAVAFX thread
        // if you don't add this it won't work :)
        Platform.runLater(() -> {
            String name = toAdd.getName();
            var places = pane.getChildren();
            for(int i = 0; i < Math.min(places.size(), playerList.size()); i++) {
                StackPane place = (StackPane) places.get(i);
                Label label = (Label) place.getChildren().get(1);
                String nextName = label.getText();
                label.setText(name);
                place.setVisible(true);
                name = nextName;
            }
            if(playerList.size() > 8) {
                morePlayersWaitingRoomLabel.setVisible(true);
                morePlayersWaitingRoomLabel.setText("and " + (playerList.size()-8) + " more players");
            }
        });
    }

    /**
     * Update the UI based on the <code>playerList</code>
     */
    public void updateUI() {
        for (Node node : pane.getChildren()) {
            node.setVisible(false); // there are 6-7 circle added by default but I hide them
        }
        morePlayersWaitingRoomLabel.setVisible(false); // hide the label
        this.playerList = serverUtils.getAllNamesInWaitingRoom(); // get request on the players that are currently waiting
        System.out.println(this.playerList);
        for (int i = 0; i < this.playerList.size(); i++) {
            movePlayers(playerList.get(i));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize called by the waiting roomCtrl");
        this.playerList = serverUtils.getAllNamesInWaitingRoom(); // get request on the players that are currently waiting
        updateUI();
        this.serverUtils.subscribeForSocketMessages("/topic/waitingRoom", Player.class, player -> {
            playerList.add(player);
            movePlayers(player);
        });
        this.serverUtils.subscribeForSocketMessages("/topic/disconnect", Player.class, player -> {
            System.out.println("Player " + player.name + " disconnected");
            playerList.remove(player);
            updateUI();
        });
    }
}
