package client.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainAppController {
    private Scene waitingRoomScene;
    private Scene enterNameScene;
    private Stage primaryStage;

    private WaitingRoomCtrl waitingRoomCtrl;
    private String name;

    public void initialize(Stage primaryStage, Pair<WaitingRoomCtrl, Parent> waitingRoomPair,
                           Pair<EnterNameCtrl, Parent> enterName){
        this.waitingRoomScene = new Scene(waitingRoomPair.getValue());
        this.enterNameScene = new Scene(enterName.getValue());
        this.primaryStage = primaryStage;

        this.waitingRoomCtrl = waitingRoomPair.getKey();

        primaryStage.setScene(this.enterNameScene);
        primaryStage.show();
    }
    public void changeScene(String name){
        this.name = name;
        System.out.println("Changing scene " + name);
        primaryStage.setScene(waitingRoomScene);
        primaryStage.show();
    }

    public String getName() {
        return name;
    }
}
