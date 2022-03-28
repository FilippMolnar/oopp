package client.controllers;

import client.IPScanner;
import client.utils.ServerUtils;
import commons.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class HomeScreenMultiplayerCtrl implements Initializable {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    private final CountDownLatch startSignal = new CountDownLatch(1);

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
        System.out.println(name);
        String finalName = name.substring(0, Math.min(name.length(), 16));

        if (name.isEmpty()) {
            labelErrors.setText("Please enter your name");
            return;
        }

        if (!name.equals(finalName)) {
            // Send message to player that their name was too long
            labelErrors.setText("Your name was too long, we limited the number of characters");
        }
        startSignal.await(); // wait for the thread in server utils to finish

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

        // Show single player. 0 would be single player.
        this.appController.showNext();
        //this.serverUtils.postName(finalName);
        this.appController.setName(finalName);
        this.serverUtils.sendThroughSocket("/app/enterRoom", new Player(finalName));
    }

    public void backToHomeScreen() {
        appController.showHomeScreen();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventHandler<ActionEvent> event = e -> {
            if (serversDropdown.getValue() != null) {
                // when a user click on a server option the 'server' connect will happen in another thread
                serverUtils.initializeServer(serversDropdown.getValue(), startSignal);
            }
        };
        serversDropdown.setOnAction(event);
        List<String> servers = IPScanner.scanServers();
        System.out.println("Servers are : " + servers);
        ObservableList<String> obsList = FXCollections.observableArrayList(servers);
        serversDropdown.setItems(obsList);
    }
}
