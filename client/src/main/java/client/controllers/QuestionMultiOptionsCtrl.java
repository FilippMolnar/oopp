package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Joker;
import commons.Player;
import commons.Question;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionMultiOptionsCtrl {
    private final ServerUtils server;
    private final MainAppController mainCtrl;
    @FXML
    GridPane parentGridPane;
    private Question question;
    @FXML
    private Button optionA;
    @FXML
    private Button optionB;
    @FXML
    private Button optionC;
    @FXML
    private GridPane images;
    @FXML
    private Arc timerArc;
    @FXML
    private Text timerValue;
    private int timerIntegerValue;

    @Inject
    public QuestionMultiOptionsCtrl(ServerUtils server, MainAppController mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setQuestion(Question question) {
        this.question = question;
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        optionA.setText(question.getChoices().get(0).getTitle());
        optionB.setText(question.getChoices().get(1).getTitle());
        optionC.setText(question.getChoices().get(2).getTitle());


        System.out.println("Printing images" + imageViews);
        for (int i = 0; i < imageViews.size(); i++) {
            var view = (ImageView) imageViews.get(i);
            var choice = question.getChoices().get(i);
            Path path = Paths.get(choice.getImagePath());
            var actualPath = getClass().getResource("/33/" + path.getFileName()).toString();
            var newImage = new Image(actualPath);
            view.setImage(newImage);

            System.out.println(path.getFileName());
            System.out.println(actualPath);
        }
    }

    public void pressedOption(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        String button_id = source.getId();
        Activity a;
        if(button_id.equals("optionA")){
            a = question.getChoices().get(0);
        } else if(button_id.equals("optionB")){
            a = question.getChoices().get(1);
        } else {
            a = question.getChoices().get(2);
        }
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);

        System.out.println(question);
        System.out.println(a.id == question.getCorrect().id);
        server.sendThroughSocket("/app/submit_answer", a.id == question.getCorrect().id);
    }

    public void firstJoker(){
        return;
//        List<Joker> jokers = mainCtrl.getJokers().getJokers();
//        if(jokers.get(0).isUsed()){
//            System.out.println("used");
//            return;
//        }
//        jokers.get(0).use();
    }

    public void secondJoker(){
        return;
//        List<Joker> jokers = mainCtrl.getJokers().getJokers();
//        if(jokers.get(1).isUsed()){
//            System.out.println("used");
//            return;
//        }
//
//        jokers.get(1).use();

    }

    public void thirdJoker(){
        List<Joker> jokers = mainCtrl.getJokers().getJokers();
        if(jokers.get(2).isUsed()){
            System.out.println("used");
            return;
        }

        ArrayList<Integer> wrong_options = new ArrayList<>();
        int i = 0;
        for(Activity a : question.getChoices()){
            if(a.id != question.getCorrect().id){
                wrong_options.add(i);
            }
            i++;
        }
        int index = (int)(Math.random() * wrong_options.size());
        System.out.println(wrong_options);
        System.out.println(index);
        switch(wrong_options.get(index)){
            case 0:
                optionA.setText("wrong");
                break;
            case 1:
                optionB.setText("wrong");
                break;
            case 2:
                optionC.setText("wrong");
                break;
        }
        jokers.get(2).use();
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
            view.setFitHeight(pane.getHeight());
            view.setFitWidth(pane.getWidth());
        }
    }


    public void startTimerAnimation() {
        timerIntegerValue = Integer.parseInt(timerValue.getText());
        timerArc.setLength(360);
        //create a timeline for moving the circle
        Timeline timeline = new Timeline();
        //You can add a specific action when each frame is started.
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
            }
        };
        TimerTask task = new TimerTask() {
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
        Timer numberTimer = new Timer();
        int durationTime = timerIntegerValue;
        numberTimer.scheduleAtFixedRate(task, 1000, 1000);

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
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished, lengthProperty);

        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        animationTimer.start();
    }


    /**
     * Wrapper function used to showcase the userReaction method with the help of a button. Will be deleted once we
     * complete the reaction functionality.
     */
    public void userReaction() {
        userReaction("angel", "Bianca");
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
}
