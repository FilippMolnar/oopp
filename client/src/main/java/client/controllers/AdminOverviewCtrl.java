package client.controllers;

import client.utils.ServerUtils;
import com.google.errorprone.annotations.FormatMethod;
import commons.Activity;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminOverviewCtrl implements Initializable {

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private TableView<Activity> activityTable;
    @FXML
    private TableColumn<Activity, String> titleColumn;
    @FXML
    private TableColumn<Activity, String> sourceColumn;
    @FXML
    private TableColumn<Activity, Integer> consumptionColumn;
    @FXML
    private TableColumn<Activity, String> imageColumn;

    @FXML
    Button exitButton;
    @FXML
    Button addButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Label error;

    @Inject
    AdminOverviewCtrl(ServerUtils serverUtils, MainAppController appController){
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

//    List<Activity> activities = serverUtils.getAllActivities();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        titleColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTitle()));
        sourceColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getSource()));
        consumptionColumn.setCellValueFactory(x -> (new SimpleIntegerProperty(x.getValue().getConsumption())).asObject());
        imageColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getImagePath()));
    }

    /**
     * Retrieves the activity the user selects from table
     * @return Activity that user selected
     */
    public Activity retrieveActivity(){
        return activityTable.getSelectionModel().getSelectedItem();
    }

    public void exit() {
        appController.showHomeScreen();
    }

    // Method to go to edit screen to add an activity
    // Linked to addButton
    public void toAddActivity() {

    }

    // Method to go to edit screen to edit an activity
    // Linked to editButton
    public void toEditActivity() {
        Activity selectedActivity = retrieveActivity();
        if (selectedActivity == null) {
            error.setVisible(true);
        }
        else {
            // Go to edit screen (MainAppController) (bring the activity)
        }
    }

    // Method to delete an activity
    // Linked to deleteButton
    public void deleteActivity() {
        Activity a = retrieveActivity();
        serverUtils.deleteActivity(a);
        activityTable.getItems().remove(a);
    }

}
