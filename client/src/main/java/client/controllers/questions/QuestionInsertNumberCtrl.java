package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.application.Platform;
import java.util.*;
import commons.Answer;
import commons.UserReaction;
import java.net.URL;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class QuestionInsertNumberCtrl extends AbstractQuestion implements ControllerInitialize {

    @FXML
    private TextField number;

    @FXML
    private Text submitButton;

    @FXML
    private Text scoreText;
    @FXML
    private Slider slider;
    @FXML
    private Text sliderValue;
    @FXML
    private GridPane images;
    @FXML
    private Text questionNumber;
    @FXML
    private Text activity;
    @FXML
    private ImageView image;
    @FXML
    protected GridPane parentGridPane;

    @Inject
    public QuestionInsertNumberCtrl(ServerUtils server, MainAppController mainCtrl) {
        super(server, mainCtrl);
    }

    @Override
    public void displayAnswers(List<Integer> answer) {
        TimerTask delay = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(mainCtrl::showNext);
            }
        };
        Timer myTimer = new Timer();
        int consumption = mainCtrl.getCorrect().getConsumption();
        sliderValue.setText("Correct answer: " + consumption + "Wh");
        slider.setValue(consumption);
        myTimer.schedule(delay, 3000); // wait for 4 seconds
    }

    private Integer getNumber() {
        var n = number.getText();
        return Integer.parseInt(n);
    }

    public void changeValueSlider() {
        sliderValue.setText((int)slider.getValue()+"");
    }

    public void submitAnswer() {
        stopTimer();
        int answer = (int) slider.getValue(); slider.setDisable(true);
        submitButton.setDisable(true);
        int newScore = calculateScore(Double.parseDouble(timerValue.getText()));
        mainCtrl.updateScore(newScore);
        if(isMultiPlayer) {
            sendAnswer(new Answer(true, answer+"", mainCtrl.getGameID(), newScore, mainCtrl.getName()));
        } else {
            scoreText.setText(mainCtrl.getScore()+"");
            displayAnswers(new ArrayList());
        }
    }

    /*
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
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.subscribeForSocketMessages("/user/queue/reactions", UserReaction.class, userReaction -> {
            System.out.println("received reaction!");
            userReaction(userReaction.getReaction(), userReaction.getUsername());
        });
        server.subscribeForSocketMessages("/user/queue/statistics", List.class, this::displayAnswers);
    }

    @Override
    public void initializeController() {
        this.informationLabel.setVisible(false);
        this.scoreText.setText(mainCtrl.getScore()+"");
        this.sliderValue.setText("0");
        startTimerAnimation(10);
        resizeImages();
        resetLogic();
        questionNumber.setText("Question " + (mainCtrl.getQuestionIndex()) + "/20");
        submitButton.setDisable(false);
        slider.setDisable(false);
        int correct = mainCtrl.getCorrect().getConsumption();
        int min = (int) Math.random()*correct;
        slider.setMin(min);
        int max = (int) ((Math.random()+1)*correct);
        slider.setMax(max);
        int middle = (min+max)/2;
        slider.setValue(middle);
        sliderValue.setText(middle + "");
    }

    public void setQuestion(Question question) {
        super.setQuestion(question);
        var choice = question.getCorrect();
        int answer = choice.getConsumption();
        activity.setText(choice.getTitle());
        Path path = Paths.get(choice.getImagePath());
        System.out.println(path.toString());
        String groupID = path.getParent().getName(0).toString();
        try {
            var actualPath = getClass().getResource("/GoodActivities/" + groupID + "/" + path.getFileName()).toString();
            var newImage = new Image(actualPath);
            image.setImage(newImage);
            System.out.println(path.getFileName() + " " + actualPath);
        } catch (NullPointerException e) {
            System.out.println("Having an issue with the image " + path.getFileName() +
                    " it can't be found on the client");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        image3.setOpacity(0.0);
        circle3.setOpacity(0.0);
    }


    /**
     * This method should be called after the scene is shown because otherwise the stackPane width/height won't exist
     * I wrapped the images into a <code>StackPane</code> that is resizable and fits the grid cell
     * After that I set the image to fit the <code>StackPane</code> without losing aspect ratio.
     */
    public void resizeImages() {
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(4).toList();
        for (Node imageView : imageViews) {
            var view = (ImageView) imageView;
            StackPane pane = (StackPane) view.getParent();
            view.setPreserveRatio(true);
            view.setFitHeight(pane.getHeight() - 5);
            view.setFitWidth(pane.getWidth() - 5);
        }
    }

    /**
     * Since there is only one instance of the controller.
     * The controller won't reset it's state when a new scene loads.
     * Thus, we need to reset everything by ourselves.
     */
    private void resetLogic() {
        this.hasSubmittedAnswer = false; // this is false at the beginning of the game
    }


    public int calculateScore(double secondsToAnswer) {
        int answerPlayer = (int) slider.getValue();
        int correctAnswer = question.getCorrect().getConsumption();

        int smallRangeLeft = (int) (correctAnswer * 0.9);
        int smallRangeRight = (int) (correctAnswer * 1.1);
        int mediumRangeLeft = (int) (correctAnswer * 0.7);
        int mediumRangeRight = (int) (correctAnswer * 1.3);
        int bigRangeLeft = (int) (correctAnswer * 0.5);
        int bigRangeRight = (int) (correctAnswer * 1.5);

        int rangeBonus = 0;
        int maxPointsRange = 50;
        if (smallRangeLeft <= answerPlayer && answerPlayer <= smallRangeRight) {
            rangeBonus = 40;
        }
        else if (mediumRangeLeft <= answerPlayer && answerPlayer <= mediumRangeRight) {
            rangeBonus = 25;
        }
        else if (bigRangeLeft <= answerPlayer && answerPlayer <= bigRangeRight) {
            rangeBonus = 15;
        }

        double maxSeconds = 10;
        int maxPointsTime = 50;
        int timeBonus = (int) Math.round(maxPointsTime * (1 - ((secondsToAnswer / maxSeconds) / 2)));

        int scoreToBeAdded = 0;
        if (answerPlayer == correctAnswer) {
            scoreToBeAdded = maxPointsRange + timeBonus;
        }
        else {
            scoreToBeAdded = timeBonus + rangeBonus;
        }
        return scoreToBeAdded;
    }
}
