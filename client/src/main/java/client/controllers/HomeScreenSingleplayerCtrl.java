package client.controllers;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import commons.Question;
import java.util.ArrayList;

import javax.inject.Inject;

public class HomeScreenSingleplayerCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TextField nameString;

    @FXML
    private Label labelErrors;

    @Inject
    HomeScreenSingleplayerCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void enterRoom(){
        String name = nameString.getText();
        System.out.println(name);
        String finalName = name.substring(0,Math.min(name.length(),16));
        if (!name.equals(finalName)) {
            // Send message to player that their name was too long
            labelErrors.setText("Your name was too long, we limited the number of characters");
        }

        ArrayList<Question> questions = serverUtils.getLeastMostQuestions();
        appController.addQuestionScenes(questions, 1);
        this.appController.setName(finalName);
        this.appController.initializeScore();
        // Show single player. 0 would be multiplayer.
        this.appController.showNext();
        this.appController.setGameMode(false);
    }

    public void backToHomeScreen() {
        appController.showHomeScreen();
    }
}
