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

        this.waitingRoomScene = new Scene(waitingRoomPair.getValue());
        Scene enterNameScene = new Scene(enterName.getValue());
        this.primaryStage = primaryStage;

        this.qInsertCtrl = qInsert.getKey();
        this.qInsert = new Scene(qInsert.getValue());
        this.qMultiCtrl = qMulti.getKey();
        this.qMulti = new Scene(qMulti.getValue());

        primaryStage.setScene(enterNameScene);
        primaryStage.show();

        enterNameScene.getStylesheets().add("client/scenes/waiting_room.css");

    }

    public void enterWaitingRoom(String name) {
        System.out.println("Changing scene " + name);
        waitingRoomScene.getStylesheets().add("client/scenes/waiting_room.css");
        primaryStage.setScene(waitingRoomScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> this.serverUtils.sendThroughSocket("/app/disconnect", new Player(name)));
    }

    public void showQuestionInsert() {
        primaryStage.setTitle("Insert Number question");
        primaryStage.setScene(qInsert);
//        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
    public void showQuestionMulti() {
        primaryStage.setTitle("Multiple choice question");
        primaryStage.setScene(qMulti);
//        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

}
