package client.controllers;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import javax.inject.Inject;

public class AdminOverviewCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TableView<Activity> activityTable;

    @Inject
    AdminOverviewCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }
}
