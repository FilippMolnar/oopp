package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;


public class QuestionMultiOptionsCtrl  implements Initializable {
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

    @FXML
    GridPane parentGridPane;

    public void pressedA() {

    }

    public void pressedB() {

    }

    public void pressedC() {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
     * @param reaction - a String that can have one of the following values: "happy", "angry", "angel"
     * @param name - the nickname of the user who reacted
     */
    public void userReaction(String reaction, String name) {
        Pane pane = new Pane();
        ImageView iv;
        Label label = new Label(name);
        Image img;
        switch(reaction) {
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
        label.setPadding(new Insets(-20,0,0,5));
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
