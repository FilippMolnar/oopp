package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Answer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import commons.Question;
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
import javafx.util.Duration;


import com.google.inject.Inject;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javafx.util.Duration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javafx.animation.*;

public class QuestionMultiOptionsCtrl extends AbstractQuestion implements ControllerInitialize {
    @FXML
    private Button optionA;
    @FXML
    private Button optionB;
    @FXML
    private Button optionC;
    @FXML
    private GridPane images;
    private boolean hasSubmittedAnswer = false;
    private int correct;

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
        super.setQuestion(question);
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        optionA.setText(question.getChoices().get(0).getTitle());
        optionB.setText(question.getChoices().get(1).getTitle());
        optionC.setText(question.getChoices().get(2).getTitle());

        if(question.getChoices().get(0).equals(question.getCorrect()))correct = 0;
        else if(question.getChoices().get(1).equals(question.getCorrect()))correct = 1;
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

                System.out.println(path.getFileName() + " " + actualPath);
            } catch (NullPointerException e) {
                System.out.println("Having an issue with the image " + path.getFileName() +
                        " it can't be found on the client");
                System.out.println(Arrays.toString(e.getStackTrace()));

            }
        }
    }

    public void showChart(List<Integer> ans,int correct)
    {
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        List<Node> charts = images.lookupAll("Rectangle").stream().limit(3).toList();

        double all = ans.get(0)+ans.get(1)+ans.get(2);

        for(int i=0;i<3;i++)
        {
            imageViews.get(i).setVisible(false);
            double h = 200.0*ans.get(i)/all;
            var bar = (Rectangle)charts.get(i);
            bar.setVisible(true);
            if(i==correct)
                bar.setFill(Paint.valueOf("green"));
            else bar.setFill(Paint.valueOf("red"));
            bar.setHeight(0);
            KeyValue heightValue = new KeyValue(bar.heightProperty(),bar.getHeight()+h);
            KeyFrame frame = new KeyFrame(Duration.millis(500),heightValue);
            Timeline timeline = new Timeline(frame);
            timeline.play();
        }

    }

    /**
     * function called when user submits an answer
     * we mark that answer as final for now.
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
        sendAnswer(new Answer(a.id == question.getCorrect().id, button_id, mainCtrl.getGameID()));
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

    @Override
    public void initializeController() {
        List<Node> charts = images.lookupAll("Rectangle").stream().limit(3).toList();
        for(var bar:charts)bar.setVisible(false);
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        for(var image:imageViews)image.setVisible(true);

        startTimerAnimation();
        resizeImages();
        hasSubmittedAnswer = false;
        System.out.println("Enabling scene");
        optionA.setDisable(false);
        optionB.setDisable(false);
        optionC.setDisable(false);
        countA.setVisible(false);
        countB.setVisible(false);
        countC.setVisible(false);
        server.subscribeForSocketMessages("/user/queue/statistics", List.class, answers -> {
            System.out.println("Received answer!!" + answers);

            showChart(answers,correct);

            countA.setVisible(true);
            countA.setText("" + answers.get(0));

            countB.setVisible(true);
            countB.setText("" + answers.get(1));

            countC.setVisible(true);
            countC.setText("" + answers.get(2));

            TimerTask delay = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {

                        System.out.println("Calling mainctrl show Next");
                        mainCtrl.showNext();
                    });

                }
            };
            stopTimer();
            Timer myTimer = new Timer();
            myTimer.schedule(delay, 4000);


        });
    }


}
