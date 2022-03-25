package client.controllers.questions;

import client.controllers.ControllerInitialize;
import client.controllers.MainAppController;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class QuestionInsertNumberCtrl extends AbstractQuestion implements ControllerInitialize {
    private Question question;

    @FXML
    private TextField number;


    @Inject
    public QuestionInsertNumberCtrl(ServerUtils server, MainAppController mainCtrl) {
        super(server, mainCtrl);
    }

    public void setQuestion(Question question) {
        super.setQuestion(question);
    }

    private Integer getNumber() {
        var n = number.getText();
        return Integer.parseInt(n);
    }
    @Override
    public void initializeController() {
        startTimerAnimation(10);
        System.out.println("Enabling scene");
    }
}
