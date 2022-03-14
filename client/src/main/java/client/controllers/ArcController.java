package client.controllers;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Arc;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ArcController implements Initializable {
    @FXML
    Arc arc;


    private double getAngle(double length) {
        if (length < 90)
            return 90 - length;
        return 360 + 90 - length;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //create a timeline for moving the circle
        arc.setStartAngle(90);
        arc.setLength(0);
        Timeline timeline = new Timeline();

        //You can add a specific action when each frame is started.
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                arc.setStartAngle(getAngle(arc.getLength()));
            }

        };

        //create a keyValue with factory: scaling the circle 2times
        KeyValue lengthProperty = new KeyValue(arc.lengthProperty(), 360);


        //create a keyFrame, the keyValue is reached at time 2s
        Duration duration = Duration.millis(4000);

        EventHandler<ActionEvent> onFinished = t -> System.out.println("animation finished!");
        KeyFrame keyFrame = new KeyFrame(duration, onFinished, lengthProperty);

        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        timer.start();
    }
}
