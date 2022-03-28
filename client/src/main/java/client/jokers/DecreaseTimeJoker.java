package client.jokers;

import client.controllers.MainAppController;
import client.controllers.questions.QuestionMultiOptionsCtrl;
import client.controllers.questions.QuestionSameAsCtrl;
import client.utils.ServerUtils;
import commons.Player;

public class DecreaseTimeJoker extends Joker{
    public DecreaseTimeJoker(String name, String imagePath, ServerUtils serverUtils) {
        super(name, imagePath, serverUtils);
    }

    public void onClick(MainAppController mainCtrl){
        if(isUsed()){
            return;
        }
        System.out.println("DecreaseTimeJoker");
        serverUtils.sendThroughSocket("/app/decrease_time", new Player(mainCtrl.getName()));
        if (mainCtrl.getCurrentScene().getController() instanceof QuestionMultiOptionsCtrl qCtrl) {
            qCtrl.getDecreaseTimeCircle().setOpacity(0.5);
            qCtrl.getDecreaseTimeImage().setOpacity(0.5);
        }
        if (mainCtrl.getCurrentScene().getController() instanceof QuestionSameAsCtrl qCtrl2) {
            qCtrl2.getDecreaseTimeCircle().setOpacity(0.5);
            qCtrl2.getDecreaseTimeImage().setOpacity(0.5);
        }
        use();
    }
}
