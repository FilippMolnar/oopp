package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import commons.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.tuple.Pair;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        for(int i = 0; i < imageViews.size(); i++){
            var view = (ImageView) imageViews.get(i);
            var choice = question.getChoices().get(i);
            Path path = Paths.get(choice.getImagePath());
            System.out.println(path.getFileName());
            var actualPath = getClass().getResource("/33/" + path.getFileName()).toString();
            System.out.println(actualPath);
            var newImage = new Image(actualPath);
            view.setImage(newImage);
        }
    }

    public void pressedA() {

    }
    public void pressedB() {

    }
    public void pressedC() {

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
