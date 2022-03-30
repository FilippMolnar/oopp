package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
    @FXML
    private Label countA;
    @FXML
    private Label countB;
    @FXML
    private Label countC;
    @FXML
    private GridPane images;

    public Button getOptionA() {
        return optionA;
    }

    public Button getOptionB() {
        return optionB;
    }

    public Button getOptionC() {
        return optionC;
    }

    //private boolean hasSubmittedAnswer = false;
    private int correct;
    private Button selectedButton;

    @FXML
    private Text questionNumber;

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
    public QuestionMultiOptionsCtrl(ServerUtils server, MainAppController mainCtrl) {
        super(server, mainCtrl);
    }

    public void setQuestion(Question question) {
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
                //view.setFitWidth(1);
                //view.setFitHeight(1);
                view.setImage(newImage);
                view.setVisible(true);
            } catch (NullPointerException e) {
                System.out.println("Having an issue with the image " + path.getFileName() +
                        " it can't be found on the client");
                System.out.println("GROUP ID: " + groupID);
                System.out.println(Arrays.toString(e.getStackTrace()));

            }
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

        if(isMultiPlayer) {
            //sendAnswer(new Answer(a.id == question.getCorrect().id, button_id, mainCtrl.getGameID()));
            sendAnswerAndUpdateScore(mainCtrl, button_id, a);
        } else {
            //checkAnswer(new Answer(a.id == question.getCorrect().id, button_id, 0, mainCtrl.getName()));
            sendAnswerAndUpdateScore(mainCtrl, button_id, a);
            if (selectedButton != null) {
                selectedButton.setDisable(true);
                selectedButton.setMouseTransparent(false);
                selectedButton.setStyle("-fx-border-width: 0;");
            }
        }
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
