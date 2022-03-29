package client.jokers;

import client.controllers.MainAppController;
import client.utils.ServerUtils;


public class GoogleJoker extends Joker{
    public GoogleJoker(String name, String imagepath, ServerUtils serverUtils)
    {
        super(name, imagepath, serverUtils);
    }
    public void onClick(MainAppController mainCtrl)
    {
        if(isUsed())return ;

        mainCtrl.openBrowser();
    }
}
