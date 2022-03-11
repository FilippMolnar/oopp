package client.controllers;

import client.scenes.template.MainCtrl;
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
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class QuestionMultiOptionsCtrl  {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public QuestionMultiOptionsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    @FXML
    GridPane parentGridPane;

    public void pressedA() {
        mainCtrl.showOverview();
    }

    public void pressedB() {
        mainCtrl.showOverview();
    }

    public void pressedC() {
        mainCtrl.showOverview();
    }

    private void clearFields() {
        return;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                pressedA();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
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
