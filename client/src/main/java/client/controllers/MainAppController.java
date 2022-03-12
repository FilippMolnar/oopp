package client.controllers;

import client.LinkedScene;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.JokersList;
import commons.Player;
import commons.Question;
import commons.QuestionType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import commons.QuestionType;
import client.LinkedScene;
import java.util.List;
import java.util.Arrays;
import java.util.List;

public class MainAppController {
    private final ServerUtils serverUtils;
    private Scene waitingRoomScene;
    private Stage primaryStage;
    private Scene homeScene;
    private Scene leaderBoardScene;

    private LinkedScene currentScene;
    private LinkedScene homeScreenLinked;

    private String name;

    private QuestionInsertNumberCtrl qInsertCtrl;
    private Scene qInsert;
    private QuestionMultiOptionsCtrl qMultiCtrl;
    private Scene qMultiScene;

    private int gameID; // Game ID that the client stores and is sent to get the question

    private JokersList jokers;

    @Inject
    MainAppController(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void initialize(Stage primaryStage, Pair<WaitingRoomCtrl, Parent> waitingRoomPair,
                           Pair<HomeScreenCtrl, Parent> home,
                           Pair<LeaderBoardCtrl, Parent> leaderBoard,
                           Pair<QuestionMultiOptionsCtrl, Parent> qMulti,
                           Pair<QuestionInsertNumberCtrl, Parent> qInsert){

        this.name = "";
        this.waitingRoomScene = new Scene(waitingRoomPair.getValue());
        this.homeScene = new Scene(home.getValue());
        this.leaderBoardScene = new Scene(leaderBoard.getValue());


        LinkedScene waitingRoomLinked = new LinkedScene(this.waitingRoomScene);
        LinkedScene leaderBoardLinked = new LinkedScene(this.leaderBoardScene);
        // replace leaderBoardLinked by the waiting screen, whose controller can load the questions
        this.currentScene = new LinkedScene(this.homeScene,
                Arrays.asList(leaderBoardLinked, waitingRoomLinked));
        this.homeScreenLinked = this.currentScene;

        this.primaryStage = primaryStage;

        this.qInsertCtrl = qInsert.getKey();
        this.qInsert = new Scene(qInsert.getValue());
        this.qMultiCtrl = qMulti.getKey();
        this.qMultiScene = new Scene(qMulti.getValue());

        this.jokers = new JokersList();

        primaryStage.setScene(homeScene);
        primaryStage.show();

        this.homeScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.qMultiScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.waitingRoomScene.getStylesheets().add("client/scenes/waiting_room.css");
    }

    public String getName(){
        return this.name;
    }
    public JokersList getJokers(){
        return this.jokers;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setGameID(int gameID){
        this.gameID = gameID;
        System.out.println(gameID);
    }

    /*
     * This method takes a list of integers indicating question types and inserts
     * them into the LinkedScene navigation.
     * @param questionTypes integers indicating question types, generated by the
     * server.
     * @param mode either 0 or 1. 0 indicates single player mode, 1 multiplayer.
     */
    public void addQuestionScenes(List<Integer> questionTypes, int mode) {
        LinkedScene current = this.currentScene;
        for(int i = 0; i < questionTypes.size(); i++) {
            if(i == 10) {
                current.addNext(new LinkedScene(this.leaderBoardScene));
                current = current.getNext();
            }
            if(questionTypes.get(i) < 2) {
                current.addNext(new LinkedScene(this.qMultiScene));
            } else {
                current.addNext(new LinkedScene(this.qInsert));
            }
            current = current.getNext();
        }
        current.addNext(new LinkedScene(this.leaderBoardScene,
                    Arrays.asList(homeScreenLinked, homeScreenLinked.getNext(mode))));
    }

    /*
     * This method shows the next scene in the list of linked scenes
     */
    public void showNext() {
        this.currentScene = this.currentScene.getNext();
        primaryStage.setScene(this.currentScene.getScene());
        if(this.currentScene.getTitle() != null) {
            primaryStage.setTitle(this.currentScene.getTitle());
        }
    }

    /*
     * @param i in case multiple scenes follow the current scene,
     * the index of the following scenes is used to specify which
     * one to show next.
     */
    public void showNext(int i) {
        this.currentScene = this.currentScene.getNext(i);
        primaryStage.setScene(this.currentScene.getScene());
        if(this.currentScene.getTitle() != null) {
            primaryStage.setTitle(this.currentScene.getTitle());
        }
        primaryStage.setOnCloseRequest(event -> this.serverUtils.sendThroughSocket("/app/disconnect", new Player(this.name)));
    }

    public void showQuestion(Question question) {
        if(question.getType() == QuestionType.Estimate){
            showQuestionInsert(question);
        }else{
            showQuestionMulti(question);
        }
    }

    public void showQuestionInsert(Question q) {
        qInsertCtrl.setQuestion(q);
        primaryStage.setTitle("Insert Number question");
        primaryStage.setScene(qInsert);
        primaryStage.show();
    }
    public void showQuestionMulti(Question q) {
        qMultiCtrl.setQuestion(q);
        primaryStage.setTitle("Multiple choice question");
        primaryStage.setScene(qMultiScene);
        primaryStage.show();
    }

    /*
     * Almost every scene has a button to return to the homescreen.
     * That button activates this function. Which switches from the
     * current scene to the homescreen.
     */
    public void showHomeScreen() {
        primaryStage.setTitle("Home");
        primaryStage.setScene(homeScene);
        primaryStage.show();
        this.currentScene = this.homeScreenLinked;
    }

}
