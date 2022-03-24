package client.controllers;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.fxml.FXML;

import javax.inject.Inject;
import java.awt.*;

import static java.lang.Integer.parseInt;

public class AdminEditCtrl {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TextField activityTitleField;
    @FXML
    private TextField activitySourceField;
    @FXML
    private TextField activityConsumptionField;
    @FXML
    private TextField activityImageField;

    @Inject
    AdminEditCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void addActivity() {
        String title = activityTitleField.getText();
        String source = activitySourceField.getText();
        int consumption = parseInt(activityConsumptionField.getText());
        String image = activityImageField.getText();
        Activity a = new Activity(title, consumption, image, source);
    }
}
