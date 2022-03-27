package client.controllers;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.TextField;
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

    // Method to go to homescreen
    // Linked to exitButton
    public void exit() {
        appController.showHomeScreen();
    }

    // Method to add activity
    // TODO: add the new activity to the overview table
    public void addActivity() {
        String title = activityTitleField.getText();
        String source = activitySourceField.getText();
        int consumption = parseInt(activityConsumptionField.getText());
        String image = activityImageField.getText();
        Activity a = new Activity(title, consumption, image, source);
        // Add to ActivityRepository
        serverUtils.addAct(a);
        // Add to table
    }

    // Method to show the to be edited activity
    public void showEditActivity(Activity activity) {
        // Show original activity
        activityTitleField.setText(activity.getTitle());
        activitySourceField.setText(activity.getSource());
        activityConsumptionField.setText(String.valueOf(activity.getConsumption()));
        activityImageField.setText(activity.getImagePath());
        // Let user edit, then submit, when submit is clicked, submitEditActivity is called
    }

    // Method to actually submit the edited activity
    // TODO: remove the original activity from both repo and table
    public void submitEditActivity() {
        // First, remove original activity (from both repo and table)
        /**
         * This is removing the old activity
         */
        String title = activityTitleField.getText();
        int consumption = Integer.valueOf(activityConsumptionField.getText());
        String imagePath = activityImageField.getText();
        String source=activitySourceField.getText();
        Activity act = new Activity(title,consumption,imagePath,source);

        serverUtils.deleteActivity(act);

        // Let user make new activity with new fields
        addActivity();
    }
}
