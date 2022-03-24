package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.*;
import javafx.application.Platform;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.lang3.tuple.Pair;

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
    private GridPane images;
    //private boolean hasSubmittedAnswer = false;
    private int correct;

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
            try {
                var actualPath = getClass().getResource("/33/" + path.getFileName()).toString();
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
            a = question.getChoices().get(0);
        } else if (button_id.equals("optionB")) {
            a = question.getChoices().get(1);
        } else {
            a = question.getChoices().get(2);
        }
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);

        if(isMultiPlayer) {
            sendAnswer(new Answer(a.id == question.getCorrect().id, button_id));
        } else {
            checkAnswer(new Answer(a.id == question.getCorrect().id, button_id));
            System.out.println("Stopping timer");
            stopTimer();
            mainCtrl.showNext();
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

    public void dummy() {
        Player player = new Player(mainCtrl.getName());
        calculateScore(player, true, 20);
    }


    private void displayAnswers(List<Integer> answerList) {
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        System.out.println("Received answer!!" + answerList);
        showChart(answerList, correct);
        List<Label> labels = List.of(countA, countB, countC);
        List<Button> options = List.of(optionA,optionB,optionC);
        Button correctOption = options.get(correct);
        correctOption.setOpacity(1);
        correctOption.setStyle("-fx-font-weight: bold;");
        for (int i = 0; i < labels.size(); i++) {
            if (answerList.get(i) > 0) {
                Label label = labels.get(i);
                label.setVisible(true);
                label.setText("" + answerList.get(i));
            }
        }
        informationLabel.setVisible(true);
        informationLabel.setText("Stats received!");

        stopTimer();

        TimerTask delay = new TimerTask() {
            @Override
            public void run() {
                correctOption.setStyle("-fx-font-weight: normal;");
                correctOption.setTextFill(Paint.valueOf("#d6d3ee"));
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
        resetUI();
        resetLogic();
        super.questionNumber.setText("Question " + (mainCtrl.getQuestionIndex()) + "/20");
        startTimerAnimation();
        System.out.println("Initializing Qmulti!");
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
