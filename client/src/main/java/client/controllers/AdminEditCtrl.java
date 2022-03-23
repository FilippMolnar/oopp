package client.controllers;

import client.utils.ServerUtils;
import javafx.fxml.FXML;

import javax.inject.Inject;
import java.awt.*;

public class AdminEditCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TextField ActivityTitleField;
    @FXML
    private TextField ActivitySourceField;

    @Inject
    AdminEditCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }
}
