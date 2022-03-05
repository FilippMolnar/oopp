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
    private Scene enterNameScene;
    private String name;

    private QuestionInsertNumberCtrl qInsertCtrl;
    private Scene qInsert;

    private QuestionMultiOptionsCtrl qMultiCtrl;
    private Scene qMulti;

    @Inject
    MainAppController(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void initialize(Stage primaryStage, Pair<WaitingRoomCtrl, Parent> waitingRoomPair,
                           Pair<HomeScreenCtrl, Parent> enterName,
                           Pair<QuestionMultiOptionsCtrl, Parent> qMulti,
                           Pair<QuestionInsertNumberCtrl, Parent> qInsert){

        this.name = "";
        this.waitingRoomScene = new Scene(waitingRoomPair.getValue());
        this.enterNameScene = new Scene(enterName.getValue());
        this.primaryStage = primaryStage;

        this.qInsertCtrl = qInsert.getKey();
        this.qInsert = new Scene(qInsert.getValue());
        this.qMultiCtrl = qMulti.getKey();
        this.qMulti = new Scene(qMulti.getValue());

        primaryStage.setScene(enterNameScene);
        primaryStage.show();

        enterNameScene.getStylesheets().add("client/scenes/waiting_room.css");

    }

    public String getName(){
        return this.name;
    }

    public void enterWaitingRoom(String name) {
        System.out.println("Changing scene " + name);
        this.name = name;
        waitingRoomScene.getStylesheets().add("client/scenes/waiting_room.css");
        primaryStage.setScene(waitingRoomScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> this.serverUtils.sendThroughSocket("/app/disconnect", new Player(name)));
    }

    public void enterSinglePlayerGame(String name) {
        System.out.println("Changing scene " + name);
        primaryStage.setScene(qInsert);
        primaryStage.show();
    }

    public void showQuestionInsert() {
        primaryStage.setTitle("Insert Number question");
        primaryStage.setScene(qInsert);
        primaryStage.show();
    }
    public void showQuestionMulti() {
        primaryStage.setTitle("Multiple choice question");
        primaryStage.setScene(qMulti);
    }

    public void showHomeScreen() {
        primaryStage.setTitle("Home");
        primaryStage.setScene(enterNameScene);
        primaryStage.show();
    }

}
