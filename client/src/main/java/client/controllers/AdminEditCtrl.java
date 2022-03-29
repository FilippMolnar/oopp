package client.controllers;

import client.utils.ServerUtils;
import commons.Activity;
import javafx.fxml.FXML;

import javax.inject.Inject;
import javafx.scene.control.TextField;

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

    Activity selectedActivity;

    @Inject
    AdminEditCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public TextField getActivityTitleField() {
        return activityTitleField;
    }

    public TextField getActivitySourceField() {
        return activitySourceField;
    }

    public TextField getActivityConsumptionField() {
        return activityConsumptionField;
    }

    public TextField getActivityImageField() {
        return activityImageField;
    }

    /**
     * Goes to homescreen when exitButton is clicked
     */
    public void exit() {
        appController.showHomeScreen();
    }

    /**
     * Adds activity to ActivityRepository
     * The activity that is added is the one currently on the edit screen
     */
    public void addActivity() {
        String title = activityTitleField.getText();
        String source = activitySourceField.getText();
        int consumption = parseInt(activityConsumptionField.getText());
        String image = activityImageField.getText();
        Activity a = new Activity(title, consumption, image, source);
        serverUtils.addAct(a);
    }

    /**
     * Shows the activity the user selected to edit
     * @param activity - The activity the user selected to edit
     */
    public void showEditActivity(Activity activity) {
        // Show original activity
        appController.showAdminEdit(activity);
        selectedActivity = activity;
        // Let user edit, then submit, when submit is clicked, submitEditActivity is called
    }

    /**
     * Submits edited activity by deleting the original activity and then adding the new version
     */
    public void submitEditActivity() {
        // Remove original activity from repo
        serverUtils.deleteActivity(selectedActivity);
        // A new activity will be added with the fields filled in at the moment the user clicks submit
        addActivity();
    }
}
