package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class QuestionInsertNumberCtrl extends AbstractQuestion {
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


    private void clearFields() {
        number.clear();
    }

    private Integer getNumber() {
        var n = number.getText();
        return Integer.parseInt(n);
    }
}
