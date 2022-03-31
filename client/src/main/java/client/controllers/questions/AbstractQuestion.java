package client.controllers.questions;

import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;


public abstract class AbstractQuestion implements Initializable {
    protected final ServerUtils server;
    protected final MainAppController mainCtrl;
    protected Question question;

    @FXML
    protected Circle circle1;
    @FXML
    protected Circle circle2;
    @FXML
    protected Circle circle3;
    @FXML
    protected ImageView image1;
    @FXML
    protected ImageView image2;
    @FXML
    protected ImageView image3;

    @FXML
    public GridPane parentGridPane;
    @FXML
    protected Arc timerArc;
    @FXML
    private Text timerValue;

    @FXML
    protected Text questionNumber;

    // TO-DO change it according to game mode
    protected boolean isMultiPlayer;

    @FXML
    protected Label informationLabel;

    @FXML
    protected Button splashButton;

    public int getTimerIntegerValue() {
        return timerIntegerValue;
    }

    private int timerIntegerValue;

    protected boolean hasSubmittedAnswer = false;

    @FXML
    protected Text scoreText;

    private Timeline timeline;
    TimerTask timerTask;
    Timer numberTimer;

    protected static boolean doublePointsJoker = false;

    @Inject
    public AbstractQuestion(ServerUtils server, MainAppController mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setGameMode(boolean isMultiPlayer) {
        this.isMultiPlayer = isMultiPlayer;
    }

    public void setQuestion(Question question) {
        this.question = question;
        hasSubmittedAnswer = false;
    }

    public void setQuestionNumber(int num) {
        this.questionNumber.setText(num + "/20");
    }

    public static void setDoublePointsJoker(boolean doublePointsJoker) {
        AbstractQuestion.doublePointsJoker = doublePointsJoker;
    }

    public void triggerJoker1() {
        mainCtrl.getJokers().getJokers().get(0).onClick(mainCtrl);
    }

    public void triggerJoker2() {
        mainCtrl.getJokers().getJokers().get(1).onClick(mainCtrl);
    }

    public void triggerJoker3() {
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
        userReaction("angry", mainCtrl.getName());
        server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "angry"));

    }

    public void angelReact() {
        String path = "/app/reactions";
        userReaction("angel", mainCtrl.getName());

        server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "angel"));
    }

    public void happyReact() {
        String path = "/app/reactions";
        userReaction("happy", mainCtrl.getName());
        server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "happy"));
    }

    public void stopTimer() {
        timeline.stop();
        timerTask.cancel();
        numberTimer.cancel();
    }

    public void cutAnimationInHalf() {
        stopTimer();
        startTimerAnimation(timerIntegerValue / 2);

    }

    public void startTimerAnimation(int length) {
        timerIntegerValue = length;
        timerArc.setLength(360);
        timerArc.setFill(Paint.valueOf("#d6d3ee"));
        timerValue.setFill(Paint.valueOf("#d6d3ee"));
        //create a timeline for moving the circle
        timeline = new Timeline();
        //You can add a specific action when each frame is started.

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timerIntegerValue--;
                    System.out.println(timerIntegerValue);
                    if (timerIntegerValue < 0) {
                        timerValue.setText(Integer.toString(0));
                    } else {
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
            if (!hasSubmittedAnswer) {
                disableOptions();
                sendAnswer(new Answer(false, ""));
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished, lengthProperty);

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void disableOptions() {
        if (mainCtrl.getCurrentScene().getController() instanceof QuestionMultiOptionsCtrl qCtrl) {
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
        mainCtrl.updateScore(newScore);
        scoreText.setText(newScore + "");
    }

    public void backToHomeScreen() {
        stopTimer();
        List<Object> answerList = new ArrayList<>(2);
        answerList.add(new Player(mainCtrl.getName()));
        answerList.add(mainCtrl.getGameID());
        server.sendThroughSocket("/app/disconnectFromGame", answerList);
        mainCtrl.showHomeScreen();
    }
    // for single player
    public int calculateScore(boolean answerCorrect, double secondsLeft) {
        int scoreToBeAdded = 0;
        double maxSeconds = 10;
        int maxPoints = 100;
        double secondsToAnswer = maxSeconds - secondsLeft;
        if (answerCorrect) {
            scoreToBeAdded = (int) Math.round(maxPoints * (1 - ((secondsToAnswer / maxSeconds) / 1.5)));
        }
        return scoreToBeAdded;
    }

    public void sendAnswerAndUpdateScore(MainAppController mainCtrl, String button_id, Activity a) {
        int score = calculateScore(a.id == question.getCorrect().id, 10 - (double) this.getTimerIntegerValue());
        if (doublePointsJoker) score = score * 2;
        setDoublePointsJoker(false);
        mainCtrl.updateScore(score);
        this.scoreText.setText("SCORE " + mainCtrl.getTotalScore());
        if (isMultiPlayer) {
            sendAnswer(new Answer(a.id == question.getCorrect().id, button_id, mainCtrl.getGameID(), score, mainCtrl.getName()));
        } else {
            checkAnswer(new Answer(a.id == question.getCorrect().id, button_id));
            System.out.println("Stopping timer");
            stopTimer();
            mainCtrl.showNext();
        }
    }

    public Circle getCircle1() {
        return circle1;
    }

    public Circle getCircle2() {
        return circle2;
    }

    public Circle getCircle3() {
        return circle3;
    }

    public ImageView getImage1() { return image1; }

    public ImageView getImage2() {
        return image2;
    }

    public ImageView getImage3() {
        return image3;
    }
}
