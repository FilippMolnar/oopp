package client.controllers;

import client.utils.ServerUtils;
import commons.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
import javafx.scene.layout.RowConstraints;

import java.util.*;

import commons.Score;
import commons.Question;
import javafx.geometry.Insets;

public class LeaderBoardCtrl implements ControllerInitialize{

    @FXML
    private GridPane spots;

    private final MainAppController appController;
    private final ServerUtils serverUtils;

    @FXML
    private Label rank1_name;
    @FXML
    private Label rank1_score;
    @FXML
    private Label rank2_name;
    @FXML
    private Label rank2_score;
    @FXML
    private Label rank3_name;
    @FXML
    private Label rank3_score;
    @FXML
    private Label rank4_name;
    @FXML
    private Label rank4_score;
    @FXML
    private Label rank5_name;
    @FXML
    private Label rank5_score;
    @FXML
    private Label rank6_name;
    @FXML
    private Label rank6_score;
    @FXML
    private Label rank7_name;
    @FXML
    private Label rank7_score;

    @FXML
    private Button rematchButton;

    @FXML
    private Button homeButton;

    private List<Label> names;
    private List<Label> scores;
    private Map<Integer, List<String>> leaderboard;

    @Inject
    LeaderBoardCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }
    public void hideBackAndRematch() {
        homeButton.setVisible(false);
        rematchButton.setVisible(false);
    }

    public void after10Questions() {
        fillWithValues();
        hideBackAndRematch();
    }

    public void fillWithValues() {
        int i = 0;
        System.out.println("Filling with values!");
        this.leaderboard = serverUtils.getLeaderboard(appController.getGameID());
        /*//Game.printLeaderboardToScreen(this.leaderboard);
        Integer[] sortedScores = (Integer[]) leaderboard.keySet().toArray(new Integer[0]);*/
        Object[] keySet = leaderboard.keySet().toArray(new Object[0]);
        List<Integer> keysInt = new ArrayList<>();
        for (Object o : keySet) {
            System.out.println(o.getClass());
            if (o instanceof String) {
                String s = (String) o;
                if (leaderboard.get(s).size() > 0) {
                    keysInt.add(Integer.parseInt(s));
                }
            }
            else {
                keysInt.add((Integer) o);
            }
        }
        Integer[] sortedScores = keysInt.toArray(new Integer[0]);
        Arrays.sort(sortedScores, Collections.reverseOrder());
            for (Integer score : sortedScores) {
                for (String name : leaderboard.get(score)) {
                    if (i < 7) {
                        names.get(i).setText(name);
                        scores.get(i).setText(score + "");
                        i++;
                    }
                    else {
                        break;
                    }
                }
            }
    }


    public void goBack(){
        this.appController.showHomeScreen();
    }

    public void rematch() {
        List<Question> questions = serverUtils.getLeastMostQuestions();
        //this.appController.showNext();
        appController.addQuestionScenes(questions, 1);
        this.appController.initializeScore();
        this.appController.showNext(1);
    }
    @Override
    public void initializeController() {
        fillWithValues();
        hideBackAndRematch();
        System.out.println("LEADERBOARD:");
        List<Score> allScores = serverUtils.getSingleLeaderboard();
        System.out.println(allScores);
        List<Node> children = spots.getChildren();
        for(Node spot : spots.getChildren()) {
            spot.setVisible(false);
        }
        for(int i = 0; i < allScores.size(); i++) {
            System.out.println(allScores.get(i));
            createLeaderboardSpot(allScores.get(i), i+1);
        }
    }

    /* A sad attempt at adding leaderboard spots manually...*/
    private void createLeaderboardSpot(Score score, int row) {
        RowConstraints newRow  = new RowConstraints();
        newRow.setPrefHeight(30.0);
        spots.getRowConstraints().add(newRow);

        GridPane a = new GridPane();
        a.setPrefWidth(458.0);
        a.setMaxWidth(500.0);
        a.getStyleClass().add("non-clickable");
        a.getStyleClass().add("client/scenes/waiting_room.css");
        GridPane.setHalignment(a, HPos.CENTER);
        GridPane.setValignment(a, VPos.CENTER);
        GridPane.setHgrow(a, Priority.ALWAYS);
        Circle c = new Circle();
        c.setFill(Paint.valueOf("#1f93ff00"));
        c.setLayoutY(25.0);
        GridPane.setMargin(c, new Insets(0, 0, 0, 13)); // was 29
        c.setRadius(16.0);
        String color;
        switch(row) {
            case 1:
               color = "cbbc50";
               break;
            case 2:
               color = "5e5c69";
               break;
            case 3:
               color = "cb5708";
               break;
            default:
               color = "d6d3ee";
               break;
        }
        c.setStroke(Paint.valueOf(color));
        c.setStrokeType(StrokeType.INSIDE);
        c.setStrokeWidth(2.0);
        Text t = new Text();
        t.setFill(Paint.valueOf("#cbbc50"));
        t.setLayoutY(29.0);
        int offset = 25 - ((int)(Math.log(row) / Math.log(10)))*4;
        GridPane.setMargin(t, new Insets(0, 0, 0, offset));
        t.setStrokeType(StrokeType.OUTSIDE);
        t.setStrokeWidth(0.0);
        t.setText(row+"");
        Text t2 = new Text();
        t2.setFill(Paint.valueOf("#d6d3ee"));
        t2.setLayoutY(32.0);
        GridPane.setMargin(t2, new Insets(0, 0, 0, 60));
        t2.setStrokeType(StrokeType.OUTSIDE);
        t2.setStrokeWidth(0.0);
        t2.setText(score.getName());
        t2.setFont(Font.font(java.awt.Font.SERIF, 18.0));
        Label l = new Label();
        l.setLayoutY(17.0);
        GridPane.setMargin(l, new Insets(0, 0, 0, 276));
        l.setText(score.getScore() + "");
        a.getChildren().add(c);
        a.getChildren().add(t);
        a.getChildren().add(t2);
        a.getChildren().add(l);
        spots.add(a, 0, row, 2, 1);
    }
}
