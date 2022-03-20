package client.jokers;

import client.controllers.MainAppController;
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

        use();
    }
}
