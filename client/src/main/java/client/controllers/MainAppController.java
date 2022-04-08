package client.controllers;

import client.LinkedScene;
import client.controllers.questions.QuestionInsertNumberCtrl;
import client.controllers.questions.QuestionMultiOptionsCtrl;
import client.controllers.questions.QuestionSameAsCtrl;
import client.jokers.JokersList;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Question;
import commons.QuestionType;
import commons.Score;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainAppController {
    private final ServerUtils serverUtils;
    private Stage primaryStage;
    private Scene sameAsScene;
    private Scene leaderBoardScene;
    private Scene qMultiScene;
    private Scene qInsertScene;
    private Scene questionTransitionScene;

    private LinkedScene currentScene;
    private LinkedScene homeScreenLinked;

    private String name;
    protected boolean isMultiPlayer = false;

    private QuestionInsertNumberCtrl qInsertCtrl;
    private QuestionMultiOptionsCtrl qMultiCtrl;
    private QuestionSameAsCtrl sameAsCtrl;
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

    public void initialize(Stage primaryStage, Pair<WaitingRoomCtrl, Scene> waitingRoomPair,
                           Pair<HomeScreenCtrl, Scene> home,
                           Pair<HomeScreenSingleplayerCtrl, Scene> homeSingleplayer,
                           Pair<HomeScreenMultiplayerCtrl, Scene> homeMultiplayer,
                           Pair<LeaderBoardCtrl, Scene> leaderBoard,
                           Pair<QuestionMultiOptionsCtrl, Scene> qMulti,
                           Pair<QuestionInsertNumberCtrl, Scene> qInsert,
                           Pair<QuestionSameAsCtrl, Scene> sameAs,
                           Pair<TransitionSceneCtrl, Scene> qTransition,
                           Pair<AdminOverviewCtrl, Scene> adminOverview,
                           Pair<AdminEditCtrl, Scene> adminEdit) {

        // initialize private variables
        this.name = "";
        this.primaryStage = primaryStage;
        this.jokers = new JokersList(serverUtils, false);


        // initializing scenes and controllers for questions
        this.leaderBoardScene = leaderBoard.getValue();
        this.leaderBoardCtrl = leaderBoard.getKey();
        this.questionTransitionScene = qTransition.getValue();
        this.qTransitionCtrl = qTransition.getKey();
        this.qInsertCtrl = qInsert.getKey();
        this.qInsertScene = qInsert.getValue();
        this.qMultiCtrl = qMulti.getKey();
        this.qMultiScene = qMulti.getValue();
        this.sameAsScene = sameAs.getValue();
        this.sameAsCtrl = sameAs.getKey();

        // initializing the scenes we need to create the first LinkedScenes
        Scene waitingRoomScene = waitingRoomPair.getValue();
        Scene homeScene = home.getValue();
        Scene homeSingleplayerScene = homeSingleplayer.getValue();
        Scene homeMultiplayerScene = homeMultiplayer.getValue();
        Scene adminOverviewScene = adminOverview.getValue();
        Scene adminEditScene = adminEdit.getValue();


        // set the Admin Edit controller of the AdminOverviewCtrl instance to the right AdminEditCtrl instance
        AdminOverviewCtrl adminOverviewCtrl = adminOverview.getKey();
        AdminEditCtrl adminEditCtrl = adminEdit.getKey();
        adminOverviewCtrl.setEditCtrl(adminEditCtrl);


        // create linked scenes of the scenes
        LinkedScene waitingRoomLinked = new LinkedScene(waitingRoomScene, waitingRoomPair.getKey());
        LinkedScene singleplayerLinked = new LinkedScene(homeSingleplayerScene, homeSingleplayer.getKey());
        LinkedScene multiplayerLinked = new LinkedScene(homeMultiplayerScene, homeMultiplayer.getKey());
        LinkedScene adminOverviewLinked = new LinkedScene(adminOverviewScene,adminOverview.getKey());
        LinkedScene adminEditLinked = new LinkedScene(adminEditScene,adminEdit.getKey());

        // link the waiting room to the multiplayer scene
        multiplayerLinked.addNext(waitingRoomLinked);

        // link the admin interfaces
        adminOverviewLinked.addNext(adminEditLinked);
        adminEditLinked.addNext(adminOverviewLinked);

        // set the current scene to the home screen and add the options to go to singleplayer, multi player and the admin interface
        this.currentScene = new LinkedScene(homeScene);
        this.homeScreenLinked = this.currentScene;
        this.currentScene.addNext(multiplayerLinked);
        this.currentScene.addNext(singleplayerLinked);
        this.currentScene.addNext(adminOverviewLinked);

        // maximize the window and set the current scene
        primaryStage.setMaximized(true);
        resizeSceneToMaximize(homeScreenLinked);
        primaryStage.setScene(homeScene);
        primaryStage.show();


        // add CSS to the scenes
        homeScene.getStylesheets().add("client/scenes/waiting_room.css");
        homeMultiplayerScene.getStylesheets().add("client/scenes/waiting_room.css");
        waitingRoomScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.qMultiScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.questionTransitionScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.sameAsScene.getStylesheets().add("client/scenes/waiting_room.css");
        this.qInsertScene.getStylesheets().add("client/scenes/waiting_room.css");
    }

    public void setJokers(JokersList jokers) {
        this.jokers = jokers;
    }

    public void setQuestionNumber(int number) {
        this.questionIndex = number;
    }
    public void openBrowser()
    {
        Desktop desktop = Desktop.getDesktop();
        try{
            URI url = new URI("https://www.google.com");
            desktop.browse(url);
        }catch(URISyntaxException | IOException e){
            e.printStackTrace();
        }
    }

    public static void playSound(String dirPath) {
        String path = "src/main/resources/client/sounds/" + dirPath;
        File[] dir = new File(path).listFiles();
        int idx = new Random().nextInt(dir.length);
        Media sound = new Media(dir[idx].toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
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
        this.score = new Score(this.name, 0);
    }

    public JokersList getJokers() {
        return this.jokers;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public Activity getCorrect() {
        return questionsInGame.get(questionIndex-2).getCorrect();
    }

    public LinkedScene getLinkedScene() {
        return this.currentScene;
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
        questionIndex = 1;
        // make sure the previous game is removed from the next scenes
        if(mode == 1) {
            homeScreenLinked.reset(mode);
        } else if (mode == 0) {
            homeScreenLinked.getNext().reset(0);
        }
        LinkedScene current = this.currentScene;
        questionsInGame = questions;
        for (int i = 0; i < questions.size(); i++) {
            if (i == 10 && mode == 0) {
                current.addNext(new LinkedScene(this.leaderBoardScene, this.leaderBoardCtrl));
                current = current.getNext();
            }
            // add the transition before a normal question
            current.addNext(new LinkedScene(this.questionTransitionScene, this.qTransitionCtrl));
            current = current.getNext();
            Question currentQuestion = questions.get(i);
            if (currentQuestion.getType() == QuestionType.HighestEnergy) {
                current.addNext(new LinkedScene(this.qMultiScene, this.qMultiCtrl));
            }
            else if (currentQuestion.getType() == QuestionType.Estimate) {
                current.addNext(new LinkedScene(this.qInsertScene, this.qInsertCtrl));
            } else {
                current.addNext(new LinkedScene(this.sameAsScene, this.sameAsCtrl));
            }
            current = current.getNext();
        }
        current.addNext(new LinkedScene(this.leaderBoardScene,
                leaderBoardCtrl));
        current.getNext().addNext(homeScreenLinked.getNext(mode));
    }

    /*
     * This method shows the next scene in the list of linked scenes
     */
    public void showNext() {
        showNext(0);
    }

    public Question getCurrentQuestion(){
        return questionsInGame.get(questionIndex-2); // -1 because of ++, -1 because of index 0
    }

    private void resizeSceneToMaximize(LinkedScene linked){
        Pane element = (Pane) linked.getScene().getRoot(); // this assumes that root of the scene is a pane
        element.setMinWidth(primaryStage.getWidth());
        element.setMinHeight(primaryStage.getHeight());
    }

    /**
     * @param i in case multiple scenes follow the current scene,
     * the index of the following scenes is used to specify which
     * one to show next.
     */
    public void showNext(int i) {
        System.out.println("SHOWING NEXT");
        this.currentScene = this.currentScene.getNext(i);
        resizeSceneToMaximize(this.currentScene);
        primaryStage.setScene(this.currentScene.getScene());
        if (this.currentScene.getTitle() != null) {
            primaryStage.setTitle(this.currentScene.getTitle());
        }
        primaryStage.show();
        Object controller = this.currentScene.getController();
        if (controller instanceof QuestionMultiOptionsCtrl qController) {
            qController.setQuestion(questionsInGame.get(questionIndex-1));
            qController.setQuestionNumber(questionIndex);
            qController.setGameMode(isMultiPlayer);
            questionIndex++;
        }
        else if (controller instanceof QuestionInsertNumberCtrl qController) {
            qController.setQuestion(questionsInGame.get(questionIndex-1));
            qController.setQuestionNumber(questionIndex);
            qController.setGameMode(isMultiPlayer);
            questionIndex++;
        }
        else if (controller instanceof QuestionSameAsCtrl qController) {
            qController.setQuestion(questionsInGame.get(questionIndex-1));
            qController.setQuestionNumber(questionIndex);
            qController.setGameMode(isMultiPlayer);
            questionIndex++;
        }
        if(controller instanceof LeaderBoardCtrl c && !isMultiPlayer) {
            // send the single player score before showing the leaderboard
            if(questionsInGame != null && questionIndex == questionsInGame.size()) {
                System.out.println("UPLOADING SCORE");
                serverUtils.addScore(score);
                questionIndex = 1;
            }
        }
        if (controller instanceof ControllerInitialize controllerInit) {
            System.out.println("INITIALIZE CONTROLLER");
            controllerInit.initializeController();
        }
    }

    /*
     * Almost every scene has a button to return to the homescreen.
     * That button activates this function. Which switches from the
     * current scene to the homescreen.
     */
    public void showHomeScreen() {
        this.isMultiPlayer = false;
        primaryStage.setTitle("Home");
        resizeSceneToMaximize(homeScreenLinked);
        primaryStage.setScene(homeScreenLinked.getScene());
        primaryStage.show();
        this.currentScene = this.homeScreenLinked;
    }

    public void updateScore(int amount) {
        this.score.addScore(amount);
    }

    public int getTotalScore() {
        return this.score.getScore();
    }

    public Map<Integer, List<String>> getLeaderboard() {
        return serverUtils.getLeaderboard(gameID);
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public boolean isMultiPlayer() {
        return this.isMultiPlayer;
    }
}
