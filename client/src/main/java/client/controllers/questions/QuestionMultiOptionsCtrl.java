package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


import javafx.util.Duration;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class QuestionMultiOptionsCtrl extends AbstractQuestion implements ControllerInitialize {


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


    @FXML
    private Text questionNumber;

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

        if (question.getChoices().get(0).id == question.getCorrect().id) correctOption = 0;
        else if (question.getChoices().get(1).id == question.getCorrect().id) correctOption = 1;
        else correctOption = 2;
        System.out.println("Correct from highest energy : "  + correctOption);


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
        final Button source = (Button) actionEvent.getSource();
        String button_id = source.getId();
        Activity a;
        if (button_id.equals("optionA")) {
            selectedOption = 0;
            a = question.getChoices().get(0);
        } else if (button_id.equals("optionB")) {
            selectedOption = 1;
            a = question.getChoices().get(1);
        } else {
            selectedOption = 2;
            a = question.getChoices().get(2);
        }
        if(a.id == question.getCorrect().id){
            System.out.println(button_id + "is Correct!");
        }else{
            System.out.println(button_id + "is Wrong!");
        }
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);

        if(isMultiPlayer) {
            sendAnswerAndUpdateScore(mainCtrl, button_id, a);
        } else {
            sendAnswerAndUpdateScore(mainCtrl, button_id, a);
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
        selectedOption = -1;
        informationLabel.setVisible(false);
        countA.setVisible(false);
        countB.setVisible(false);
        countC.setVisible(false);
        resizeImages();
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

    /**
     * function called when each question is rendered
     */
    @Override
    public void initializeController() {
        this.scoreText.setText("SCORE " + mainCtrl.getScore());
        questionNumber.setText("Question " + (mainCtrl.getQuestionIndex()) + "/20");
        startTimerAnimation(10);
        resetUI();
        resetLogic();
        super.questionNumber.setText("Question " + (mainCtrl.getQuestionIndex()) + "/20");
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
