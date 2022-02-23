package client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingRoomCtrl implements Initializable {

    private final MainAppController appController;

    @FXML
    private AnchorPane pane;

    private int index = 0;

    @Inject
    WaitingRoomCtrl(MainAppController appController){
        this.appController = appController;
    }


    public void setNewName(String newName){
        System.out.println("Initialize called by the waiting roomCtrl");
        for(Node node : pane.getChildren()){
            node.setVisible(false);
        }
        StackPane child = (StackPane)pane.getChildren().get(index);
        this.index++;
        Label label = (Label)child.getChildren().get(1);
        System.out.println(newName);
        label.setText(newName);
        child.setVisible(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
