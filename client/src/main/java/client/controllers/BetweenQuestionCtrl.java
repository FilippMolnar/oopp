package client.controllers;

import client.utils.ServerUtils;

import javax.inject.Inject;

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
