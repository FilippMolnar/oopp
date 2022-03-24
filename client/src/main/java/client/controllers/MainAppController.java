package client.controllers;

import client.LinkedScene;
import client.controllers.questions.QuestionInsertNumberCtrl;
import client.controllers.questions.QuestionMultiOptionsCtrl;
import client.controllers.questions.QuestionSameAsCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import client.jokers.JokersList;
import commons.Question;
import commons.Score;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

public class MainAppController {
    private final ServerUtils serverUtils;
    private Scene waitingRoomScene;
    private Stage primaryStage;
    private Scene homeScene;
    private Scene leaderBoardScene; private Scene qMultiScene;
    private Scene qInsert;
    private Scene questionTransitionScene;
    private Scene sameAsScene;

    private LinkedScene currentScene;
    private LinkedScene homeScreenLinked;

    private String name;
    protected boolean isMultiPlayer;

    private QuestionInsertNumberCtrl qInsertCtrl;
    private QuestionMultiOptionsCtrl qMultiCtrl;
    private LeaderBoardCtrl leaderBoardCtrl;
    private TransitionSceneCtrl qTransitionCtrl;

    private int gameID; // Game ID that the client stores and is sent to get the question
    private Score score;

    private List<Question> questionsInGame;
    private int questionIndex = 0;
    private JokersList jokers;

    @Inject
    MainAppController(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void initialize(Stage primaryStage, Pair<WaitingRoomCtrl, Parent> waitingRoomPair,
                           Pair<HomeScreenCtrl, Parent> home,
                           Pair<LeaderBoardCtrl, Parent> leaderBoard,
                           Pair<QuestionMultiOptionsCtrl, Parent> qMulti,
                           Pair<QuestionInsertNumberCtrl, Parent> qInsert,
                           Pair<QuestionSameAsCtrl, Parent> sameAs,
                           Pair<TransitionSceneCtrl, Parent> qTransition) {

        this.name = "";
        this.waitingRoomScene = new Scene(waitingRoomPair.getValue());
        this.homeScene = new Scene(home.getValue());
        this.leaderBoardScene = new Scene(leaderBoard.getValue());
        this.questionTransitionScene = new Scene(qTransition.getValue());
        this.leaderBoardCtrl = leaderBoard.getKey();
        this.qTransitionCtrl = qTransition.getKey();

        this.sameAsScene = new Scene(sameAs.getValue());

        LinkedScene waitingRoomLinked = new LinkedScene(this.waitingRoomScene);
        LinkedScene leaderBoardLinked = new LinkedScene(this.leaderBoardScene);
        LinkedScene sameAsLinked = new LinkedScene(this.sameAsScene);
        // replace leaderBoardLinked by the waiting screen, whose controller can load the questions
        this.currentScene = new LinkedScene(this.homeScene);
        currentScene.addNext(waitingRoomLinked);
        this.homeScreenLinked = this.currentScene;

        this.primaryStage = primaryStage;

        this.qInsertCtrl = qInsert.getKey();
        this.qTransitionCtrl = qTransition.getKey();
        this.qInsert = new Scene(qInsert.getValue());
        this.qMultiCtrl = qMulti.getKey();
        this.qMultiScene = new Scene(qMulti.getValue());
        jokers = new JokersList(serverUtils);

        primaryStage.setScene(homeScene);
        primaryStage.show();

        this.homeScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.qMultiScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.waitingRoomScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.questionTransitionScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.sameAsScene.getStylesheets().add("client/scenes/waiting_room.css");
    }

    public String getName() {
        return this.name;
    }

    public LinkedScene getCurrentScene() {
        return currentScene;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return this.score.getScore();
    }

    public void setScore(int score) {
        this.score.setScore(score);
    }

    public void addScore(int toAdd) {
        this.score.addScore(toAdd);
    }

    public void initializeScore() {
        System.out.println("INITIALIZING SCORE");
        this.score = new Score(this.name, 0);
    }

    public JokersList getJokers() {
        return this.jokers;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
        System.out.println(gameID);
    }

    public int getGameID() {
        return this.gameID;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setGameMode(boolean isMultiPlayer) {
        this.isMultiPlayer = isMultiPlayer;
    }

    /**
     * This method takes a list of actual question  and inserts
     * them into the LinkedScene navigation.
     *
     * @param questions List of questions objects, generated by the server
     * @param mode      either 0 or 1. 0 indicates single player mode, 1 multiplayer.
     **/
    public void addQuestionScenes(List<Question> questions, int mode) {
        // make sure the previous game is removed from the next scenes
        homeScreenLinked.reset(1);
        LinkedScene current = this.currentScene;
        questionsInGame = questions;
        for (int i = 0; i < questions.size(); i++) {
            if (i == 10 && mode == 0) {
                current.addNext(new LinkedScene(this.leaderBoardScene));
                current = current.getNext();
            } else {
                // add the transition before a normal question
                current.addNext(new LinkedScene(this.questionTransitionScene, this.qTransitionCtrl));
                if(i == 0 && mode == 1) {
                    current = current.getNext(1);
                } else {
                    current = current.getNext();
                }
            }
            //            if(questionTypes.get(i) < 2) {
            //                current.addNext(new LinkedScene(this.qMultiScene, this.qMultiCtrl));
            //            } else {
            //                current.addNext(new LinkedScene(this.qInsert, this.qInsertCtrl));
            //            }
            current.addNext(new LinkedScene(this.qMultiScene, this.qMultiCtrl));
            current.addNext(new LinkedScene(this.questionTransitionScene, this.qTransitionCtrl));
            current = current.getNext();
        }
        current.addNext(new LinkedScene(this.leaderBoardScene,
                    leaderBoardCtrl));
        current.getNext().addNext(homeScreenLinked.getNext());
    }

    /*
     * This method shows the next scene in the list of linked scenes
     */
    public void showNext() {
        this.currentScene = this.currentScene.getNext();

        primaryStage.setScene(this.currentScene.getScene());
        if (this.currentScene.getTitle() != null) {
            primaryStage.setTitle(this.currentScene.getTitle());
        }
        primaryStage.show();
        Object controller = this.currentScene.getController();
        // if this controller is of the question then set the question
        if (controller instanceof QuestionMultiOptionsCtrl qController) {
            qController.setQuestion(questionsInGame.get(questionIndex));
            questionIndex++;
            qController.setQuestionNumber(questionIndex);
            qController.setGameMode(isMultiPlayer);
        }
        // if this controller is of the question then set the question
        else if (controller instanceof QuestionInsertNumberCtrl qController) {
            qController.setQuestion(questionsInGame.get(questionIndex));
            questionIndex++;
            qController.setQuestionNumber(questionIndex);
            qController.setGameMode(isMultiPlayer);
        }
        if (controller instanceof ControllerInitialize controllerInit) {
            controllerInit.initializeController();
            if(questionIndex == questionsInGame.size()) {
                System.out.println(serverUtils.addScore(score));
                questionIndex = 0;
            }
        }
    }

    public Question getCurrentQuestion(){
        return questionsInGame.get(questionIndex-1);
    }

    /*
     * @param i in case multiple scenes follow the current scene,
     * the index of the following scenes is used to specify which
     * one to show next.
     */
    public void showNext(int i) {
        this.currentScene = this.currentScene.getNext(i);

        primaryStage.setScene(this.currentScene.getScene());
        if (this.currentScene.getTitle() != null) {
            primaryStage.setTitle(this.currentScene.getTitle());
        }
        primaryStage.show();
        Object controller = this.currentScene.getController();
        // if this controller is of the question then set the question
        if (controller instanceof QuestionMultiOptionsCtrl qController) {
            qController.setQuestion(questionsInGame.get(questionIndex));
            questionIndex++;
            qController.setQuestionNumber(questionIndex);
            qController.setGameMode(isMultiPlayer);
        }
        // if this controller is of the question then set the question
        else if (controller instanceof QuestionInsertNumberCtrl qController) {
            qController.setQuestion(questionsInGame.get(questionIndex));
            questionIndex++;
            qController.setQuestionNumber(questionIndex);
            qController.setGameMode(isMultiPlayer);
        }
        if (controller instanceof ControllerInitialize controllerInit) {
            System.out.println("Calling initialize!!!");
            controllerInit.initializeController();
            if (questionIndex == questionsInGame.size()) {
                System.out.println(serverUtils.addScore(score));
                questionIndex = -1;
            }
        }
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
