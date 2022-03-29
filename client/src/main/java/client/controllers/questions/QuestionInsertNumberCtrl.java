package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class QuestionInsertNumberCtrl extends AbstractQuestion implements ControllerInitialize {

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
    private Circle elimWrongAnswerCircle;
    @FXML
    private Circle doublePointsCircle;
    @FXML
    private Circle decreaseTimeCircle;
    @FXML
    private ImageView elimWrongAnswerImage;
    @FXML
    private ImageView doublePointsImage;
    @FXML
    private ImageView decreaseTimeImage;

    @Inject
    public QuestionInsertNumberCtrl(ServerUtils server, MainAppController mainCtrl) {
        super(server, mainCtrl);
    }

    @Override
    public void initializeController() {
        this.scoreText.setText("SCORE " + mainCtrl.getScore());
        startTimerAnimation(10);
        resizeImages();
        resetLogic();
        System.out.println("Initializing insert number");
        questionNumber.setText("Question " + (mainCtrl.getQuestionIndex()) + "/20");
    }

    public void setQuestion(Question question) {
        super.setQuestion(question);
        var choice = question.getCorrect();
        int answer = choice.getConsumption();
        activity.setText(choice.getTitle());
        Path path = Paths.get(choice.getImagePath());
        System.out.println(path.toString());
        try {
            var actualPath = getClass().getResource("/33/" + path.getFileName()).toString();
            var newImage = new Image(actualPath);
            image.setFitWidth(1);
            image.setFitHeight(1);
            image.setImage(newImage);
            System.out.println(path.getFileName() + " " + actualPath);
        } catch (NullPointerException e) {
            System.out.println("Having an issue with the image " + path.getFileName() +
                    " it can't be found on the client");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        double randomMin = Math.random();
        double randomMax = 1 + Math.random();
        int min = (int) (answer * randomMin);
        int max = (int) (answer * randomMax);
        slider.setMin(min);
        slider.setMax(max + 1);
        elimWrongAnswerImage.setOpacity(0.0);
        elimWrongAnswerCircle.setOpacity(0.0);
    }

    @FXML
    private void changeValueSlider() {
        sliderValue.setText((int) slider.getValue() + "");
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


    @Override
    public int calculateScore(boolean answerCorrect, double secondsToAnswer) {
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

    public Circle getDoublePointsCircle() {
        return doublePointsCircle;
    }

    public Circle getDecreaseTimeCircle() {
        return decreaseTimeCircle;
    }

    public ImageView getDoublePointsImage() {
        return doublePointsImage;
    }

    public ImageView getDecreaseTimeImage() {
        return decreaseTimeImage;
    }

}
