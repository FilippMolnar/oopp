package client.controllers.questions;

import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Answer;
import commons.Question;
import commons.UserReaction;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;


public abstract class AbstractQuestion implements Initializable {
    protected final ServerUtils server;
    protected final MainAppController mainCtrl;
    protected Question question;

    @FXML
    protected Button optionA;
    @FXML
    protected Button optionB;
    @FXML
    protected Button optionC;
    @FXML
    protected Label countA;
    @FXML
    protected Label countB;
    @FXML
    protected Label countC;
    @FXML
    protected GridPane images;

    @FXML
    protected GridPane parentGridPane;
    @FXML
    protected Arc timerArc;
    @FXML
    protected Text timerValue;
    @FXML
    protected Text score;
    @FXML
    protected Text questionNumber;

    protected boolean isMultiPlayer;

    @FXML
    protected Label informationLabel;

    private int timerIntegerValue;
    protected int correct;

    protected boolean hasSubmittedAnswer = false;

    protected Timeline timeline;
    TimerTask timerTask;
    Timer numberTimer;


    @Inject
    public AbstractQuestion(ServerUtils server, MainAppController mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void showChart(List<Integer> ans, int correct) {
        System.out.println("SHOWING CHART");
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        List<Node> charts = images.lookupAll("Rectangle").stream().limit(3).toList();

        double all = ans.get(0) + ans.get(1) + ans.get(2);

        for (int i = 0; i < 3; i++) {
            imageViews.get(i).setVisible(false);
            double h = 150 * ans.get(i) / all;
            var bar = (Rectangle) charts.get(i);
            bar.setVisible(true);
            bar.setOpacity(1);
            if (i == correct)
                bar.setFill(Paint.valueOf("#95BF74"));
            else bar.setFill(Paint.valueOf("#C56659"));
            bar.setHeight(0);
            KeyValue heightValue = new KeyValue(bar.heightProperty(), bar.getHeight() + h);
            KeyFrame frame = new KeyFrame(Duration.millis(500), heightValue);
            Timeline timeline2 = new Timeline(frame);
            timeline2.play();
        }
    }

    public void displayAnswers(List<Integer> answerList) {
        if(!(mainCtrl.getCurrentScene().getController().getClass() == getClass())) {
            return;
        }
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        System.out.println("Received answer!!" + answerList);
        if(isMultiPlayer) {
            showChart(answerList, correct);
        }
        List<Label> labels = List.of(countA, countB, countC);
        List<Button> options = List.of(optionA,optionB,optionC);
        Button correctOption = options.get(correct);
        correctOption.setOpacity(1);
        correctOption.setStyle("-fx-font-weight: bold;");
        if(isMultiPlayer) {
            for (int i = 0; i < labels.size(); i++) {
                if (answerList.get(i) > 0) {
                    Label label = labels.get(i);
                    label.setVisible(true);
                    label.setText("" + answerList.get(i));
                }
            }
        }
        informationLabel.setVisible(true);
        informationLabel.setText("Stats received!");

        // For some reason, commenting this fixes bugs and doesn't break anything (???)
       // TimerTask delay = new TimerTask() {
       //     @Override
       //     public void run() {
       //         correctOption.setStyle("-fx-font-weight: normal;");
       //         correctOption.setTextFill(Paint.valueOf("#d6d3ee"));
       //         Platform.runLater(mainCtrl::showNext);
       //     }
       // };
       // Timer myTimer = new Timer();
       // myTimer.schedule(delay, 3000000); // wait for 4 seconds
    }

    public void setGameMode(boolean isMultiPlayer) {
        this.isMultiPlayer = isMultiPlayer;
    }

    public void setQuestion(Question question) { this.question = question; hasSubmittedAnswer = false;
    }

    public void setQuestionNumber(int num) {
        this.questionNumber.setText(num + "/20");
    }

    public void triggerJoker1(){
        mainCtrl.getJokers().getJokers().get(0).onClick(mainCtrl);
    }
    public void triggerJoker2(){
        mainCtrl.getJokers().getJokers().get(1).onClick(mainCtrl);
    }
    public void triggerJoker3(){
        mainCtrl.getJokers().getJokers().get(2).onClick(mainCtrl);
    }

    public void initialize(URL location, ResourceBundle resources) {
        server.subscribeForSocketMessages("/user/queue/reactions", UserReaction.class, userReaction -> userReaction(userReaction.getReaction(), userReaction.getUsername()));
    }

    /**
     * Animates the reactions of users.
     *
     * @param reaction - a String that can have one of the following values: "happy", "angry", "angel"
     * @param name     - the nickname of the user who reacted
     */
    /*
    public void userReaction(String reaction, String name) {
        Pane pane = new Pane();
        ImageView iv;
        Label label = new Label(name);
        String imagePath = "/client/pictures/" + reaction;
        Image img = new Image(getClass().getResource(imagePath).toString());

        iv = new ImageView(img);
        pane.getChildren().add(iv);
        pane.getChildren().add(label);
        label.setPadding(new Insets(-20, 0, 0, 5));
        TranslateTransition translate = new TranslateTransition();
        translate.setByY(200);
        translate.setDuration(Duration.millis(2000));
        translate.setNode(pane);
        translate.play();

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(2000));
        fade.setFromValue(10);
        fade.setToValue(0);
        fade.setNode(pane);
        fade.play();
        parentGridPane.getChildren().add(pane);
    }
    */
    public void userReaction(String reaction, String name) {

        Pane pane = new Pane();
        ImageView iv;
        Label label = new Label(name);
        Image img;
        switch (reaction) {
            case "happy":
                img = new Image(getClass().getResource("/client/pictures/happy.png").toString());
                break;
            case "angry":
                img = new Image(getClass().getResource("/client/pictures/angry.png").toString());
                break;
            case "angel":
                img = new Image(getClass().getResource("/client/pictures/angel.png").toString());
                break;
            default:
                return;
        }
        iv = new ImageView(img);
        pane.getChildren().add(iv);
        pane.getChildren().add(label);
        iv.setMouseTransparent(false);
        label.setMouseTransparent(false);
        label.setPadding(new Insets(-20, 0, 0, 5));
        TranslateTransition translate = new TranslateTransition();
        translate.setByY(700);
        translate.setDuration(Duration.millis(2800));
        translate.setNode(pane);
        translate.setOnFinished(t -> {
            System.out.println("deleted");
            pane.getChildren().remove(iv);
            pane.getChildren().remove(label);
        }
        );
        translate.play();

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(2000));
        //fade.setDelay(Duration.millis(1000));
        fade.setFromValue(10);
        fade.setToValue(0);
        fade.setNode(pane);
        fade.play();
        parentGridPane.getChildren().add(pane);

    }

    public void angryReact() {
        String path = "/app/reactions";
        userReaction("angry",mainCtrl.getName());
        server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "angry"));
    }

    public void angelReact() {
        String path = "/app/reactions";
        userReaction("angel",mainCtrl.getName());
        server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "angel"));
    }

    public void happyReact() {
        String path = "/app/reactions";
        userReaction("happy",mainCtrl.getName());
        server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "happy"));
    }

    public void stopTimer(){
        timeline.stop();
        timerTask.cancel();
        numberTimer.cancel();
    }

    public void cutAnimationInHalf(){
        stopTimer();
        startTimerAnimation(timerIntegerValue/2);

    }

    public void startTimerAnimation(int length) {
        timerIntegerValue = length;
        timerArc.setLength(360);
        timerArc.setFill(Paint.valueOf("#d6d3ee"));
        timerValue.setFill(Paint.valueOf("#d6d3ee"));
        //create a timeline for moving the circle
        System.out.println("TIMELINE INITIALIZED");
        this.timeline = new Timeline();
        //You can add a specific action when each frame is started.

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timerIntegerValue--;
                    System.out.println(timerIntegerValue);
                    if(timerIntegerValue < 0){
                        timerValue.setText(Integer.toString(0));
                    } else{
                        timerValue.setText(Integer.toString(timerIntegerValue));
                    }
                    if (timerIntegerValue <= 3) {
                        timerArc.setFill(Paint.valueOf("red")); // set the color to red when the timer runs out
                    }
                });
            }
        };
        numberTimer = new Timer();
        timerValue.setText(Integer.toString(length));
        numberTimer.scheduleAtFixedRate(timerTask, 1000, 1000);

        //create a keyValue with factory: scaling the circle 2times
        KeyValue lengthProperty = new KeyValue(timerArc.lengthProperty(), 0);


        //create a keyFrame, the keyValue is reached at time 2s
        System.out.println(timerValue.getText());
        Duration duration = Duration.millis(length * 1000);

        EventHandler<ActionEvent> onFinished = t -> {
            System.out.println("animation finished!");
            numberTimer.cancel();
            timerIntegerValue = 0;
            timerValue.setText("0");
            if (!hasSubmittedAnswer){
                disableOptions();
                System.out.println("time out");
                if(isMultiPlayer) {
                    sendAnswer(new Answer(false, "", mainCtrl.getGameID()));
                } else {
                    displayAnswers(new ArrayList());
                }
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished, lengthProperty);


        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    public void disableOptions(){
        if(mainCtrl.getCurrentScene().getController() instanceof QuestionMultiOptionsCtrl qCtrl){
            qCtrl.getOptionA().setDisable(true);
            qCtrl.getOptionB().setDisable(true);
            qCtrl.getOptionC().setDisable(true);
        }
    }

    /**
     * send answer to the server
     *
     * @param answer true/false depending if the selected answer was good
     */
    public void sendAnswer(Answer answer) {
        informationLabel.setVisible(true);
        informationLabel.setText("Answer submitted!");
        hasSubmittedAnswer = true;
        stopTimer();
        server.sendThroughSocket("/app/submit_answer", answer);
    }

    public void checkAnswer(Answer answer) {
        int newScore = calculateScore(answer.isCorrect(), Double.parseDouble(timerValue.getText()));
        mainCtrl.setScore(newScore);
        score.setText(newScore+"");
    }

    /**
     * Wrapper function used to showcase the userReaction method with the help of a button. Will be deleted once we
     * complete the reaction functionality.
     */
    public void userReaction() {
        userReaction("angel", "Bianca");
    }

    // for single player
    public int calculateScore(boolean answerCorrect, double secondsLeft) {
        int currentScore = mainCtrl.getScore();

        int scoreToBeAdded = 0;
        double maxSeconds = 10;
        int maxPoints = 100;
        double secondsToAnswer = maxSeconds - secondsLeft;
        if (answerCorrect) {
            scoreToBeAdded = (int) Math.round(maxPoints * (1 - ((secondsToAnswer / maxSeconds) / 1.5)));
        }
        System.out.println(scoreToBeAdded);
        return currentScore + scoreToBeAdded;
    }

}
