package client.controllers;

import client.LinkedScene;
import client.controllers.questions.QuestionInsertNumberCtrl;
import client.controllers.questions.QuestionMultiOptionsCtrl;
import client.utils.ServerUtils;
import commons.Player;
import commons.Question;
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

public class WaitingRoomCtrl implements Initializable, ControllerInitialize {

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
        System.out.println("The player list is " + this.playerList);
        for (Player player : this.playerList) {
            movePlayers(player);
        }
    }

    public void goBack() {
        serverUtils.sendThroughSocket("/app/disconnect", new Player(this.appController.getName()));
        this.appController.showHomeScreen();
    }

    public void startGame() {
        serverUtils.sendThroughSocket("/app/startGame", new Player(this.appController.getName()));
        serverUtils.postStartGame();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize called by the waiting roomCtrl");



    }

    @Override
    public void initializeController() {
        updateUI();
        System.out.println("-----------------------------------------------------");
        this.serverUtils.subscribeForSocketMessages("/topic/waitingRoom", Player.class, player -> {
            playerList.add(player);
            movePlayers(player);
        });
        this.serverUtils.subscribeForSocketMessages("/topic/disconnect", Player.class, player -> {
            System.out.println("Player " + player.getName() + " disconnected");
            playerList.remove(player);
            updateUI();
        });

        this.serverUtils.subscribeForSocketMessages("/user/queue/startGame/gameID", Integer.class, gameID -> {
            appController.setGameID(gameID);
            List<Question> questions = serverUtils.getAllGameQuestions(gameID);
            appController.addQuestionScenes(questions, 0);
            appController.showNext();

            // disconnect from waiting room
            this.serverUtils.sendThroughSocket("/app/disconnect", new Player(appController.getName()));
        });
        this.serverUtils.subscribeForSocketMessages("/user/queue/decrease_time/gameID", Integer.class, gameID -> {
            System.out.println("decreased");
            LinkedScene current = appController.getCurrentScene();
            if(current.getController() instanceof QuestionMultiOptionsCtrl qCtrl){
                System.out.println("cutting animation");
                qCtrl.cutAnimationInHalf();
            } else if(current.getController() instanceof QuestionInsertNumberCtrl qCtrl){
                qCtrl.cutAnimationInHalf();
            }
        });


    }
}
