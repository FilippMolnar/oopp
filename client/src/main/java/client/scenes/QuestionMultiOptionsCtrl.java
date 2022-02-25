package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.scene.input.KeyEvent;

public class QuestionMultiOptionsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public QuestionMultiOptionsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void pressedA() {
        mainCtrl.showOverview();
    }
    public void pressedB() {
        mainCtrl.showOverview();
    }
    public void pressedC() {
        mainCtrl.showOverview();
    }

    private void clearFields() {
        return;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                pressedA();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}
