package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Question;
import commons.UserReaction;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class QuestionMultiOptionsCtrl extends AbstractQuestion implements ControllerInitialize {
    @FXML
    private Button optionA;
    @FXML
    private Button optionB;
    @FXML
    private Button optionC;

    public Button getOptionA() {
        return optionA;
    }

    public Button getOptionB() {
        return optionB;
    }

    public Button getOptionC() {
        return optionC;
    }

    @FXML
    private GridPane images;
    //private boolean hasSubmittedAnswer = false;
    private int correct;
    private Button selectedButton;

    @FXML
    private Text questionNumber;

    @FXML
    private Label countA;
    @FXML
    private Label countB;
    @FXML
    private Label countC;

    @Inject
    public QuestionMultiOptionsCtrl(ServerUtils server, MainAppController mainCtrl) {
        super(server, mainCtrl);
    }

    public void setQuestion(Question question) {
        System.out.println("question");
        System.out.println(question);
        super.setQuestion(question);
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        optionA.setText(question.getChoices().get(0).getTitle());
        optionB.setText(question.getChoices().get(1).getTitle());
        optionC.setText(question.getChoices().get(2).getTitle());

        if (question.getChoices().get(0).equals(question.getCorrect())) correct = 0;
        else if (question.getChoices().get(1).equals(question.getCorrect())) correct = 1;
        else correct = 2;

        for (int i = 0; i < imageViews.size(); i++) {
            var view = (ImageView) imageViews.get(i);
            var choice = question.getChoices().get(i);
            Path path = Paths.get(choice.getImagePath());
            String groupID = path.getParent().getName(0).toString();
            try {
                var actualPath = getClass().getResource("/GoodActivities/" + groupID + "/" + path.getFileName()).toString();
                var newImage = new Image(actualPath);
                view.setFitWidth(1);
                view.setFitHeight(1);
                view.setImage(newImage);
                view.setVisible(true);
            } catch (NullPointerException e) {
                System.out.println("Having an issue with the image " + path.getFileName() +
                        " it can't be found on the client");
                System.out.println(Arrays.toString(e.getStackTrace()));

            }
        }
    }

    public void showChart(List<Integer> ans, int correct) {
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
            Timeline timeline = new Timeline(frame);
            timeline.play();
        }

    }

    /**
     * function called when user submits an answer
     * we mark that answer as final for now.
     *
     * @param actionEvent event used to get the button
     */
    public void pressedOption(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        String button_id = source.getId();
        Activity a;
        if (button_id.equals("optionA")) {
            selectedButton = optionA;
            a = question.getChoices().get(0);
        } else if (button_id.equals("optionB")) {
            selectedButton = optionB;
            a = question.getChoices().get(1);
        } else {
            selectedButton = optionC;
            a = question.getChoices().get(2);
        }
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        sendAnswerAndUpdateScore(mainCtrl, button_id, a);
    }


    /**
     * This method should be called after the scene is shown because otherwise the stackPane width/height won't exist
     * I wrapped the images into a <code>StackPane</code> that is resizable and fits the grid cell
     * After that I set the image to fit the <code>StackPane</code> without losing aspect ratio.
     */
    public void resizeImages() {
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        for (Node imageView : imageViews) {
            var view = (ImageView) imageView;
            StackPane pane = (StackPane) view.getParent();
            view.setPreserveRatio(true);
            view.setFitHeight(pane.getHeight() - 5);
            view.setFitWidth(pane.getWidth() - 5);
        }
    }

    public int calculateScore(boolean answerCorrect, double secondsToAnswer) {
        int scoreToBeAdded = 0;
        double maxSeconds = 10;
        int maxPoints = 100;
        if (answerCorrect) {
            scoreToBeAdded = (int) Math.round(maxPoints * (1 - ((secondsToAnswer / maxSeconds) / 1.5)));
        }
        return scoreToBeAdded;
    }

    private void displayAnswers(List<Integer> answerList) {
        if(!(mainCtrl.getCurrentScene().getController() instanceof QuestionMultiOptionsCtrl)){
            System.out.println("No longer on this scene");
            return;
        }
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        System.out.println("Received answer!!" + answerList);
        showChart(answerList, correct);
        List<Label> labels = List.of(countA, countB, countC);
        List<Button> options = List.of(optionA, optionB, optionC);
        Button correctOption = options.get(correct);
        correctOption.setDisable(false);
        correctOption.setMouseTransparent(true);
        if (selectedButton != null) {
            selectedButton.setDisable(false);
            selectedButton.setMouseTransparent(true);
            selectedButton.setStyle("-fx-border-width: 2.4; -fx-border-color: #C56659");
        }
        correctOption.setStyle("-fx-border-width: 2.4; -fx-font-weight: bold; -fx-border-color: #83b159");

        for (int i = 0; i < labels.size(); i++) {
            if (answerList.get(i) > 0) {
                Label label = labels.get(i);
                label.setVisible(true);
                label.setText("" + answerList.get(i));
            }
        }
        informationLabel.setVisible(true);
        informationLabel.setText("Stats received!");

        //stopTimer();

        TimerTask delay = new TimerTask() {
            @Override
            public void run() {
                correctOption.setDisable(true);
                correctOption.setMouseTransparent(false);
                correctOption.setStyle("-fx-border-width: 0; -fx-font-weight: normal;");
                if (selectedButton != null) {
                    selectedButton.setDisable(true);
                    selectedButton.setMouseTransparent(false);
                    selectedButton.setStyle("-fx-border-width: 0;");
                }
                Platform.runLater(mainCtrl::showNext);
            }
        };
        Timer myTimer = new Timer();
        myTimer.schedule(delay, 3000); // wait for 4 seconds
    }

    /**
     * This method should be called whenever this scene is shown to make sure the buttons are hidden and images resize etc.
     */
    private void resetUI() {
        List<Node> charts = images.lookupAll("Rectangle").stream().limit(3).toList();
        selectedButton = null;
        for(int  i=0;i<3;i++)
        {
            var bar = (Rectangle)charts.get(i);
            bar.setVisible(false);
        }
        informationLabel.setVisible(false);
        countA.setVisible(false);
        countB.setVisible(false);
        countC.setVisible(false);
        resizeImages();
        System.out.println("Enabling scene");
        optionA.setDisable(false);
        optionB.setDisable(false);
        optionC.setDisable(false);
    }

    /**
     * Since there is only one instance of the controller.
     * The controller won't reset it's state when a new scene loads.
     * Thus, we need to reset everything by ourselves.
     */
    private void resetLogic() {
        this.hasSubmittedAnswer = false; // this is false at the beginning of the game
    }

    /**
     * function called when each question is rendered
     */
    @Override
    public void initializeController() {
        System.out.println("Initializing Qmulti!");
        this.scoreText.setText("SCORE " + mainCtrl.getScore());
        questionNumber.setText("Question " + (mainCtrl.getQuestionIndex()) + "/20");
        startTimerAnimation(10);
        resetUI();
        resetLogic();
        super.questionNumber.setText("Question " + (mainCtrl.getQuestionIndex()) + "/20");
        System.out.println("Initializing Qmulti!");
        showJokerImages();
    }

    /**
     * Called only once by javafx. I register the socket messages here because if I would do this for
     * each question we would get duplicate function registrations for the same event.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.subscribeForSocketMessages("/user/queue/reactions", UserReaction.class, userReaction -> {
            System.out.println("received reaction!");
            userReaction(userReaction.getReaction(), userReaction.getUsername());
        });
        server.subscribeForSocketMessages("/user/queue/statistics", List.class, this::displayAnswers);
    }
}
