package client.controllers;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class HomeScreenCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TextField nameString;

    @FXML
    private Label labelErrors;

    @Inject
    HomeScreenCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void enterSinglePlayer() {
        appController.setGameMode(false);
        appController.setQuestionNumber(1);
        System.out.println("entering singleplayer screen");
        appController.showNext(1);
    }

    public void enterMultiPlayer() {
        appController.setGameMode(true);
        appController.setQuestionNumber(1);
        System.out.println("entering multiplayer screen");
        appController.showNext(0);
    }

    public void showAdminOverview() {
        System.out.println("entering admin screen");
        appController.showNext(2);
    }

    public void showLeaderBoard() {
        appController.showNext(3);
        LeaderBoardCtrl ctrl = (LeaderBoardCtrl) appController.getLinkedScene().getController();
        ctrl.getRematchButton().setOpacity(0);
        ctrl.getRematchButton().setDisable(true);
    }
}
