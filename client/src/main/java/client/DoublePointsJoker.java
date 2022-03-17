package client;

import client.controllers.MainAppController;
import client.utils.ServerUtils;

public class DoublePointsJoker extends Joker{
    public DoublePointsJoker(String name, String imagePath, ServerUtils serverUtils) {
        super(name, imagePath, serverUtils);
    }

    public void onClick(MainAppController mainCtrl){
        if(isUsed()){
            return;
        }
        System.out.println("DoublePointsJoker");
        use();
    }
}
