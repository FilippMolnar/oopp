package client.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainAppController {
    private Scene waitingRoomScene;
    private Stage primaryStage;

    public void initialize(Stage primaryStage, Pair<WaitingRoomCtrl, Parent> waitingRoomPair,
                           Pair<EnterNameCtrl, Parent> enterName){
        this.waitingRoomScene = new Scene(waitingRoomPair.getValue());
        Scene enterNameScene = new Scene(enterName.getValue());
        this.primaryStage = primaryStage;

        primaryStage.setScene(enterNameScene);
        primaryStage.show();
    }
    public void changeScene(String name){
        System.out.println("Changing scene " + name);
        primaryStage.setScene(waitingRoomScene);
        primaryStage.show();
    }

}
