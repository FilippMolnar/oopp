package client.controllers;

import client.utils.ServerUtils;
import commons.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingRoomCtrl implements Initializable {

    private final MainAppController appController;
    private final ServerUtils serverUtils;
    private List<Player> nameList = new ArrayList<>();

    @FXML
    private AnchorPane pane;

    private int index = 0;

    @Inject
    WaitingRoomCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void addName(Player player) {
        nameList.add(player);
        System.out.println("Adding new player " + player);
        displayPlayer(player.name, nameList.size() - 1);
    }

    public void displayPlayer(String playerName, int index) {
        try {
            StackPane child = (StackPane) pane.getChildren().get(index);
            Label label = (Label) child.getChildren().get(1);
            System.out.println(playerName);
            label.setText(playerName);
            child.setVisible(true);
        } catch (Exception e) {
            throw new IllegalArgumentException("The index " + index + " is out of range for the stackpane uf");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize called by the waiting roomCtrl");
        for (Node node : pane.getChildren()) {
            node.setVisible(false);
        }
//        this.nameList = serverUtils.getAllNamesInWaitingRoom();
//        for(int i = 0; i < this.nameList.size(); i++){
//            displayPlayer(this.nameList.get(i),i);
//        }
        this.serverUtils.registerForMessages("/topic/waitingRoom", player -> {
            addName(player);
            System.out.println("Receiving a message!");
        });
    }
}
