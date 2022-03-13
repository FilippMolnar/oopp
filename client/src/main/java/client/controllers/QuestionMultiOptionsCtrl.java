package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Joker;
import commons.Player;
import commons.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.tuple.Pair;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuestionMultiOptionsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainAppController mainCtrl;
    private Question question;

    @FXML
    private Button optionA;

    @FXML
    private Button optionB;

    @FXML
    private Button optionC;


    @FXML
    private GridPane images;

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

    public void pressedA() {

    }

    public void pressedB() {

    }

    public void pressedC() {

    }

    public void firstJoker() {
        return;
//        List<Joker> jokers = mainCtrl.getJokers().getJokers();
//        if(jokers.get(0).isUsed()){
//            System.out.println("used");
//            return;
//        }
//        jokers.get(0).use();
    }

    public void secondJoker() {
        return;
//        List<Joker> jokers = mainCtrl.getJokers().getJokers();
//        if(jokers.get(1).isUsed()){
//            System.out.println("used");
//            return;
//        }
//
//        jokers.get(1).use();

    }

    public void thirdJoker() {
        List<Joker> jokers = mainCtrl.getJokers().getJokers();
        if (jokers.get(2).isUsed()) {
            System.out.println("used");
            return;
        }

        ArrayList<Integer> wrong_options = new ArrayList<>();
        int i = 0;
        for (Activity a : question.getChoices()) {
            if (a.id != question.getCorrect().id) {
                wrong_options.add(i);
            }
            i++;
        }
        int index = (int) (Math.random() * wrong_options.size());
        System.out.println(wrong_options);
        System.out.println(index);
        switch (wrong_options.get(index)) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}


