package client.controllers;

import client.utils.ServerUtils;
import javax.inject.Inject;

public class LeaderBoardCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @Inject
    LeaderBoardCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void goBack(){
        this.appController.showHomeScreen();
    }

    public void rematch() {
        this.appController.showNext();
    }
}
