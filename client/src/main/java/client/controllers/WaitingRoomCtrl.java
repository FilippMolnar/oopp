package client.controllers;

import client.utils.ServerUtils;
import commons.Player;
import commons.Question;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
    private GridPane pane;

    @FXML
    private Label morePlayersWaitingRoomLabel;


    @Inject
    WaitingRoomCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }


    /**
     * Moves all players one to the right and adds one player up front.
     *
     * @param toAdd the player to add at the front
     */
    public void movePlayers(Player toAdd) {
        // this Platform.runLater() is used to make sure that this code is run on the JAVAFX thread
        // if you don't add this it won't work :)
        Platform.runLater(() -> {
            String name = toAdd.getName();
            int numOfPlaces = pane.getChildren().size();
            var places = pane.getChildren();
            for (int i = 0; i < Math.min(numOfPlaces, playerList.size()); i++) {
                StackPane place = (StackPane) places.get(i);
                Label label = (Label) place.getChildren().get(1);
                String nextName = label.getText();
                label.setText(name);
                place.setVisible(true);
                name = nextName;
            }
            if (playerList.size() > numOfPlaces) {
                morePlayersWaitingRoomLabel.setVisible(true);
                morePlayersWaitingRoomLabel.setText("and " + (playerList.size() - numOfPlaces) + " more players");
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
        for (Player player : this.playerList) {
            movePlayers(player);
        }
    }

    public void goBack(){
        serverUtils.sendThroughSocket("/app/disconnect", new Player(this.appController.getName()));
        this.appController.showHomeScreen();
    }

    public void startGame(){
        serverUtils.sendThroughSocket("/app/startGame", new Player(this.appController.getName()));
        serverUtils.postStartGame();
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
        this.serverUtils.subscribeForSocketMessages("/user/queue/startGame", Question.class, question -> {
            System.out.println("Received a question to render");
            Platform.runLater(() -> appController.showQuestion(question));
        });

        this.serverUtils.subscribeForSocketMessages("/topic/render_question", Player.class, player -> {
            System.out.println("Rendering question type: " + player);
            this.renderQuestion();
        });

//        this.serverUtils.subscribeForSocketMessages("/user/queue/startGame", Integer.class, gameID -> {
//            System.out.println("Want to switch scenes!");
//            Platform.runLater(appController::showQuestionInsert);
//        });
    }

    private void renderQuestion() {
        this.appController.showQuestionMulti();
    }
}
