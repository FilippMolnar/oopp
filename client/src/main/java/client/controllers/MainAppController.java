package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainAppController {
    private final ServerUtils serverUtils;
    private Scene waitingRoomScene;
    private Stage primaryStage;

    @Inject
    MainAppController(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void initialize(Stage primaryStage, Pair<WaitingRoomCtrl, Parent> waitingRoomPair,
                           Pair<HomeScreenCtrl, Parent> enterName) {
        this.waitingRoomScene = new Scene(waitingRoomPair.getValue());
        Scene enterNameScene = new Scene(enterName.getValue());
        this.primaryStage = primaryStage;

        primaryStage.setScene(enterNameScene);
        primaryStage.show();
    }

    public void enterWaitingRoom(String name) {
        System.out.println("Changing scene " + name);
        waitingRoomScene.getStylesheets().add("client/scenes/waiting_room.css");
        primaryStage.setScene(waitingRoomScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> this.serverUtils.sendThroughSocket("/app/disconnect", new Player(name)));
    }


}
