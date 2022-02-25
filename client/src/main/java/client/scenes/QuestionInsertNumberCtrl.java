package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class QuestionInsertNumberCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField number;

    @Inject
    public QuestionInsertNumberCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void submit() {
        mainCtrl.showOverview();
    }

    private void clearFields() {
        number.clear();
    }

    private Integer getNumber() {
        var n = number.getText();
        return Integer.parseInt(n);
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                submit();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}
