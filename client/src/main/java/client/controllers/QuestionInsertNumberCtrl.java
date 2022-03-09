package client.controllers;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class QuestionInsertNumberCtrl {
    private final ServerUtils server;
    private final MainAppController mainCtrl;
    private Question question;

    @FXML
    private TextField number;




    @Inject
    public QuestionInsertNumberCtrl(ServerUtils server, MainAppController mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    private void clearFields() {
        number.clear();
    }

    private Integer getNumber() {
        var n = number.getText();
        return Integer.parseInt(n);
    }

}
