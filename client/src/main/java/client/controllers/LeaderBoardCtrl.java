package client.controllers;

import client.utils.ServerUtils;
import javax.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import java.util.List;
import commons.Score;

public class LeaderBoardCtrl implements ControllerInitialize{

    @FXML
    private GridPane spots;

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @Inject
    LeaderBoardCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }

    public void goBack(){
        this.appController.showHomeScreen();
    }

    public void rematch() {
        this.appController.showNext();
    }

    public void initializeController() {
        System.out.println("LEADERBOARD:");
        List<Score> allScores = serverUtils.getSingleLeaderboard();
        System.out.println(allScores);
        List<Node> children = spots.getChildren();
        for(Node spot : spots.getChildren()) {
            spot.setVisible(false);
        }
        for(int i = 0; i < allScores.size(); i++) {
            System.out.println(allScores.get(i));
            createLeaderboardSpot(allScores.get(i), i); 
        }
    }

    /* A sad attempt at adding leaderboard spots manually...*/
    private void createLeaderboardSpot(Score score, int row) {
        GridPane a = new GridPane(); 
        a.setMaxHeight(50.0);
        //a.setMaxWidth(500.0);
        a.setMaxWidth(458.0);
        a.setPrefWidth(458.0);
        a.getStyleClass().add("non-clickable");
        a.getStyleClass().add("client/scenes/waiting_room.css");
        GridPane.setHalignment(a, HPos.CENTER);
        GridPane.setValignment(a, VPos.CENTER);
        GridPane.setHgrow(a, Priority.ALWAYS);
        Circle c = new Circle();
        c.setFill(Paint.valueOf("#1f93ff00"));
        c.setLayoutX(29.0);
        c.setLayoutY(25.0);
        c.setRadius(16.0);
        c.setStroke(Paint.valueOf("cbbc50"));
        c.setStrokeType(StrokeType.INSIDE);
        c.setStrokeWidth(2.0);
        Text t = new Text();
        t.setFill(Paint.valueOf("#cbbc50"));
        t.setLayoutX(25.0);
        t.setLayoutY(29.0);
        t.setStrokeType(StrokeType.OUTSIDE);
        t.setStrokeWidth(0.0);
        t.setText("1");
        Text t2 = new Text();
        t2.setFill(Paint.valueOf("#d6d3ee"));
        t2.setLayoutX(60.0);
        t2.setLayoutY(32.0);
        t2.setStrokeType(StrokeType.OUTSIDE);
        t2.setStrokeWidth(0.0);
        t2.setText(score.getName());
        t2.setFont(Font.font(java.awt.Font.SERIF, 18.0));
        Label l = new Label();
        l.setLayoutX(276.0);
        l.setLayoutY(17.0);
        l.setText(score.getScore() + "");
        a.getChildren().add(c);
        a.getChildren().add(t);
        a.getChildren().add(t2);
        a.getChildren().add(l);
        spots.add(a, 0, row, 2, 1);
    }
}
