package client.controllers;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class AdminOverviewCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @Inject
    AdminOverviewCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }
}
