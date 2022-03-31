package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Answer;
import commons.Question;
import commons.UserReaction;
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

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class QuestionSameAsCtrl extends AbstractQuestion implements ControllerInitialize {
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
    @FXML
    private Text activity;
    @FXML
    private ImageView answerImage;


    private Button selectedButton;

    public Button getOptionA() {
        return optionA;
    }

    public Button getOptionB() {
        return optionB;
    }

    public Button getOptionC() {
        return optionC;
    }

    private boolean hasSubmittedAnswer = false;

    @Inject
    public QuestionSameAsCtrl(ServerUtils server, MainAppController mainCtrl) {
        super(server, mainCtrl);
    }

    public void setQuestion(Question question) {
        super.setQuestion(question);
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(4).toList();
        optionA.setText(question.getChoices().get(0).getTitle());
        optionB.setText(question.getChoices().get(1).getTitle());
        optionC.setText(question.getChoices().get(2).getTitle());
        activity.setText(question.getChoices().get(3).getTitle());

        if (question.getChoices().get(0).equals(question.getCorrect())) correct = 0;
        else if (question.getChoices().get(1).equals(question.getCorrect())) correct = 1;
        else correct = 2;

        for (int i = 0; i < 3; i++) {
            var view = (ImageView) imageViews.get(i);
            var choice = question.getChoices().get(i);
            Path path = Paths.get(choice.getImagePath());
            String groupID = path.getParent().getName(0).toString();
            try {
                var actualPath = getClass().getResource("/GoodActivities/" + groupID + "/" + path.getFileName()).toString();
                //var actualPath = getClass().getResource("/33/" + path.getFileName()).toString();
                var newImage = new Image(actualPath);
                view.setFitWidth(1);
                view.setFitHeight(1);
                view.setImage(newImage);

                System.out.println(path.getFileName() + " " + actualPath);
            } catch (NullPointerException e) {
                System.out.println("Having an issue with the image " + path.getFileName() +
                        " it can't be found on the client");
                System.out.println("GROUP ID: " + groupID);
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        Path path = Paths.get(question.getChoices().get(3).getImagePath());
        String groupID = path.getParent().getName(0).toString();
        var actualPath = getClass().getResource("/GoodActivities/" + groupID + "/" + path.getFileName()).toString();
        var newImage = new Image(actualPath);
        answerImage.setImage(newImage);
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
            a = this.question.getChoices().get(0);
        } else if (button_id.equals("optionB")) {
            selectedButton = optionB;
            a = this.question.getChoices().get(1);
        } else {
            selectedButton = optionC;
            a = this.question.getChoices().get(2);
        }
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);

        if(isMultiPlayer) {
            sendAnswerAndUpdateScore(mainCtrl, button_id, a);
            //sendAnswer(new Answer(a.id == question.getCorrect().id, button_id, mainCtrl.getGameID()));
        } else {
            checkAnswer(new Answer(a.id == question.getCorrect().id, button_id, mainCtrl.getGameID(), 0, mainCtrl.getName()));
            stopTimer();
            displayAnswers(new ArrayList());
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
            if(view.getParent() instanceof GridPane pane){
                if(pane.getId() != null && pane.getId().equals("answerImage")) {
                    view.setPreserveRatio(true);
                    view.setFitHeight(pane.getHeight() - 5);
                    view.setFitWidth(pane.getWidth() - 5);
                }
            }
            else if(view.getParent() instanceof StackPane pane) {
                view.setVisible(true); // fix not visible image
                view.setPreserveRatio(true);
                view.setFitHeight(pane.getHeight() - 5);
                view.setFitWidth(pane.getWidth() - 5);
            }
        }
    }

    private void resetUI() {
        informationLabel.setVisible(false);
        countA.setVisible(false);
        countB.setVisible(false);
        countC.setVisible(false);
        resetChart();
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

    @Override
    public void initializeController() {
        scoreText.setText(mainCtrl.getScore()+"");
        startTimerAnimation(10);
        resizeImages();
        hasSubmittedAnswer = false;
        optionA.setDisable(false);
        optionB.setDisable(false);
        optionC.setDisable(false);
        resetUI();
        resetLogic();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.subscribeForSocketMessages("/user/queue/reactions", UserReaction.class, userReaction -> {
            System.out.println("received reaction!");
            userReaction(userReaction.getReaction(), userReaction.getUsername());
        });
        server.subscribeForSocketMessages("/user/queue/statistics", List.class, this::displayAnswers);
    }
}
