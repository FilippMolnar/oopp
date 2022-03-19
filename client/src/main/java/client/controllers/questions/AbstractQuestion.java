package client.controllers.questions;

import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Answer;
import commons.Player;
import commons.Question;
import commons.UserReaction;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.lang3.tuple.Pair;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public abstract class AbstractQuestion implements Initializable {
    protected final ServerUtils server;
    protected final MainAppController mainCtrl;
    protected Question question;

    @FXML
    GridPane parentGridPane;
    @FXML
    private Arc timerArc;
    @FXML
    private Text timerValue;
    private int timerIntegerValue;

    private boolean hasSubmittedAnswer = false;
    private final boolean afterFXMLLOAD = false;

    private AnimationTimer animationTimer;
    private Timeline timeline;
    TimerTask timerTask;
    Timer numberTimer;
    @Inject
    public AbstractQuestion(ServerUtils server, MainAppController mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void triggerJoker1(){
        mainCtrl.getJokers().getJokers().get(0).onClick();
    }
    public void triggerJoker2(){
        mainCtrl.getJokers().getJokers().get(1).onClick();
    }
    public void triggerJoker3(){
        mainCtrl.getJokers().getJokers().get(2).onClick();
    }

    public void initialize(URL location, ResourceBundle resources) {
        server.subscribeForSocketMessages("/user/queue/reactions", UserReaction.class, userReaction -> {
            userReaction(userReaction.getReaction(), userReaction.getUsername());
        });
    }

    /**
     * Animates the reactions of users.
     *
     * @param reaction - a String that can have one of the following values: "happy", "angry", "angel"
     * @param name     - the nickname of the user who reacted
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
        animationTimer.stop();
        timerTask.cancel();
        numberTimer.cancel();
    }

    public void startTimerAnimation() {
        timerIntegerValue = 10;
        timerArc.setLength(360);
        timerArc.setFill(Paint.valueOf("#d6d3ee"));
        timerValue.setFill(Paint.valueOf("#d6d3ee"));
        //create a timeline for moving the circle
        timeline = new Timeline();
        //You can add a specific action when each frame is started.
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
            }
        };
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timerIntegerValue--;
                    System.out.println(timerIntegerValue);
                    timerValue.setText(Integer.toString(timerIntegerValue));
                    if (timerIntegerValue <= 3) {
                        timerArc.setFill(Paint.valueOf("red")); // set the color to red when the timer runs out
                    }
                });
            }
        };
        numberTimer = new Timer();
        int durationTime = 10;
        timerValue.setText(Integer.toString(durationTime));
        numberTimer.scheduleAtFixedRate(timerTask, 1000, 1000);

        //create a keyValue with factory: scaling the circle 2times
        KeyValue lengthProperty = new KeyValue(timerArc.lengthProperty(), 0);


        //create a keyFrame, the keyValue is reached at time 2s
        System.out.println(timerValue.getText());
        Duration duration = Duration.millis(durationTime * 1000);

        EventHandler<ActionEvent> onFinished = t -> {
            System.out.println("animation finished!");
            numberTimer.cancel();
            timerIntegerValue = 0;
            timerValue.setText("0");
            if (!hasSubmittedAnswer)
                sendAnswer(new Answer(false, "", mainCtrl.getGameID()));

        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished, lengthProperty);

        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        animationTimer.start();
    }

    /**
     * send answer to the server
     *
     * @param answer true/false depending if the selected answer was good
     */
    public void sendAnswer(Answer answer) {
        hasSubmittedAnswer = true;
        server.sendThroughSocket("/app/submit_answer", answer);
    }

    public void calculateScore(Player player, boolean answerCorrect, int secondsToAnswer) {
        int currentScore = server.getGameMapping(mainCtrl.getGameID()).getScore(player);

        int scoreToBeAdded = 0;
        int maxSeconds = 20;
        int maxPoints = 100;
        if (answerCorrect) {
            scoreToBeAdded = Math.round(maxPoints * (1 - ((secondsToAnswer / maxSeconds) / 2)));
        }

        Integer score = currentScore + scoreToBeAdded;
        Pair<Player, Integer> result = Pair.of(player, score);
        server.postGameScore(mainCtrl.getGameID(), result);
    }

}


