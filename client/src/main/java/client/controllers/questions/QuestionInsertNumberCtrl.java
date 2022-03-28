package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import java.util.*;
import commons.Answer;
import commons.UserReaction;
import java.net.URL;

public class QuestionInsertNumberCtrl extends AbstractQuestion implements ControllerInitialize {
    private Question question;

    @FXML
    private TextField number;

    @FXML
    private Slider slider;

    @FXML
    private Button submitButton;

    @FXML
    private Label sliderValue;


    @Inject
    public QuestionInsertNumberCtrl(ServerUtils server, MainAppController mainCtrl) {
        super(server, mainCtrl);
    }

    public void setQuestion(Question question) {
        super.setQuestion(question);
    }

    public void displayAnswer(List<Integer> answer) {
        TimerTask delay = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(mainCtrl::showNext);
            }
        };
        Timer myTimer = new Timer();
        myTimer.schedule(delay, 3000); // wait for 4 seconds
        System.out.println(mainCtrl.getCorrect());
    }

    private Integer getNumber() {
        var n = number.getText();
        return Integer.parseInt(n);
    }

    public void changeSliderValue() {
        sliderValue.setText((int)slider.getValue()+"");
    }

    public void submitAnswer() {
        stopTimer();
        int answer = (int) slider.getValue(); slider.setDisable(true);
        submitButton.setDisable(true);
        int newScore = calculateScore(answer, Double.parseDouble(timerValue.getText()));
        System.out.println("NEW SCORE: " + newScore);
        mainCtrl.setScore(newScore);
        if(isMultiPlayer) {
            sendAnswer(new Answer(true, answer+"", mainCtrl.getGameID()));
        } else {
            score.setText(newScore+"");
            stopTimer();
            mainCtrl.showNext();
        }
    }

    public int calculateScore(int answer, double secondsLeft) {
        int currentScore = mainCtrl.getScore();

        int scoreToBeAdded = 0;
        double maxSeconds = 10;
        System.out.println(Math.abs(1 - (double)answer /
                (double)mainCtrl.getCorrect().getConsumption()) + "");
        int maxPoints = (int)(150.0 * Math.abs(1 - (double)answer /
                (double)mainCtrl.getCorrect().getConsumption()));
        double secondsToAnswer = maxSeconds - secondsLeft;
        scoreToBeAdded = (int) Math.round(maxPoints * (1 - ((secondsToAnswer / maxSeconds) / 2)));
        System.out.println(scoreToBeAdded);
        return currentScore + scoreToBeAdded;
    }

    @Override
    public void initializeController() {
        this.hasSubmittedAnswer = false;
        score.setText(mainCtrl.getScore()+"");
        slider.setDisable(false);
        submitButton.setDisable(false);
        int correct = mainCtrl.getCorrect().getConsumption();
        slider.setMin(Math.random()*correct);
        slider.setMax((Math.random()+1)*correct);
        startTimerAnimation(10);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.subscribeForSocketMessages("/user/queue/reactions", UserReaction.class, userReaction -> {
            System.out.println("received reaction!");
            userReaction(userReaction.getReaction(), userReaction.getUsername());
        });
        server.subscribeForSocketMessages("/user/queue/statistics", List.class, this::displayAnswer);
    }
}
