package client.controllers;

import client.utils.ServerUtils;
import commons.Player;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.List;

public class BetweenQuestionCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @Inject
    BetweenQuestionCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void next() {
        appController.showNext(); 
    }
}
