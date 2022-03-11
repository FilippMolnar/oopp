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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
