package client.jokers;

import client.controllers.MainAppController;
import client.controllers.questions.AbstractQuestion;
import client.controllers.questions.QuestionInsertNumberCtrl;
import client.controllers.questions.QuestionMultiOptionsCtrl;
import client.controllers.questions.QuestionSameAsCtrl;
import client.utils.ServerUtils;
import commons.Player;

public class DecreaseTimeJoker extends Joker{
    public DecreaseTimeJoker(String name, String imagePath, ServerUtils serverUtils) {
        super(name, imagePath, serverUtils);
    }

    public void onClick(MainAppController mainCtrl){
        if (isUsed()){
            return;
        }
        markUsed(mainCtrl);
        System.out.println("DecreaseTimeJoker");
        Player p = new Player(mainCtrl.getName());
        p.setGameID(mainCtrl.getGameID());
        serverUtils.sendThroughSocket("/app/decrease_time", p);

        use();
    }

    public static void decreaseTime(AbstractQuestion qCtrl){
        qCtrl.cutAnimationInHalf();
    }
}
