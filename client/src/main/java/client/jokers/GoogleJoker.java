package client.jokers;

import client.controllers.MainAppController;
import client.controllers.questions.AbstractQuestion;
import client.utils.ServerUtils;
import commons.Player;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class GoogleJoker extends Joker{
    public GoogleJoker(String name, String imagepath, ServerUtils serverUtils)
    {
        super(name, imagepath, serverUtils);
    }
    public void onClick(MainAppController mainCtrl)
    {
        if(isUsed())return ;
        playSound("google");
        Player p = new Player(mainCtrl.getName());
        p.setGameID(mainCtrl.getGameID());
        serverUtils.sendThroughSocket("/app/increase_time", p);

        Desktop desktop = Desktop.getDesktop();
        try{
            URI url = new URI("https://www.google.com");
            desktop.browse(url);
        }catch(URISyntaxException | IOException e){
            e.printStackTrace();
        }

        use();
        markUsed(mainCtrl);
    }
    public static void increaseTime(AbstractQuestion qCtrl)
    {
        qCtrl.addTimeForGoogling();
    }
}
