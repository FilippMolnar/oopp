package client.jokers;

import client.controllers.MainAppController;
import client.controllers.questions.*;
import client.utils.ServerUtils;

public class DoublePointsJoker extends Joker{
    public DoublePointsJoker(String name, String imagePath, ServerUtils serverUtils) {
        super(name, imagePath, serverUtils);
    }

    public void onClick(MainAppController mainCtrl){
        if (isUsed()) {
            return;
        }
        System.out.println("DoublePointsJoker");
        AbstractQuestion.setDoublePointsJoker(true);
        if (mainCtrl.getCurrentScene().getController() instanceof QuestionMultiOptionsCtrl qCtrl) {
            qCtrl.getDoublePointsCircle().setOpacity(0.5);
            qCtrl.getDoublePointsImage().setOpacity(0.5);
        }
        if (mainCtrl.getCurrentScene().getController() instanceof QuestionSameAsCtrl qCtrl2) {
            qCtrl2.getDoublePointsCircle().setOpacity(0.5);
            qCtrl2.getDoublePointsImage().setOpacity(0.5);
        }
        if (mainCtrl.getCurrentScene().getController() instanceof QuestionInsertNumberCtrl qCtrl3) {
            qCtrl3.getDoublePointsCircle().setOpacity(0.5);
            qCtrl3.getDoublePointsImage().setOpacity(0.5);
        }
        use();
    }
}
