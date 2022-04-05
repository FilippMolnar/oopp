package client.jokers;

import client.controllers.MainAppController;
import client.controllers.questions.AbstractQuestion;
import client.utils.ServerUtils;
import commons.Player;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class BarrelRollJoker extends Joker{
    public BarrelRollJoker(String name, String imagePath, ServerUtils serverUtils) {
        super(name, imagePath, serverUtils);
    }

    public void onClick(MainAppController mainCtrl){
        if(isUsed()){
            return;
        }
        Player p = new Player(mainCtrl.getName());
        p.setGameID(mainCtrl.getGameID());
        serverUtils.sendThroughSocket("/app/barrel_roll", p);

        System.out.println("Barrel Roll Joker");
        use();
        markUsed(mainCtrl);
    }

    public static void barrelRoll(AbstractQuestion qCtrl) {
        System.out.println("Animating the barrel roll");
        GridPane gridPane = qCtrl.parentGridPane;
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(1500));
        rotateTransition.setByAngle(360);
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setNode(gridPane);
        rotateTransition.setCycleCount(3);
        rotateTransition.setAutoReverse(true);
        rotateTransition.play();
    }
}

