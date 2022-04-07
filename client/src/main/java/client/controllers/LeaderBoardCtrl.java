package client.controllers;

import client.utils.ServerUtils;
import commons.Game;
import commons.Question;
import commons.Score;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


import javax.inject.Inject;
import java.util.*;

import commons.Score;
import commons.Question;
import javafx.geometry.Insets;
import javafx.util.Duration;

public class LeaderBoardCtrl implements ControllerInitialize {

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
    private Label rank8_name;
    @FXML
    private Label rank8_score;

    @FXML
    private Button rematchButton;
    @FXML
    private Button homeButton;

    @FXML
    private GridPane rank1_pane;
    @FXML
    private GridPane rank2_pane;
    @FXML
    private GridPane rank3_pane;
    @FXML
    private GridPane rank4_pane;
    @FXML
    private GridPane rank5_pane;
    @FXML
    private GridPane rank6_pane;
    @FXML
    private GridPane rank7_pane;
    @FXML
    private GridPane rank8_pane;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane parentGridPane;

    @FXML
    private Line loadingLine;

    @FXML
    private GridPane you_pane;
    @FXML
    private Label your_score;
    @FXML
    private Text your_rank;

    private List<Label> names;
    private List<Label> scores;
    private Map<Integer, List<String>> leaderboard;
    private List<GridPane> panes;

    @Inject
    LeaderBoardCtrl(MainAppController appController, ServerUtils serverUtils) {
        this.appController = appController;
        this.serverUtils = serverUtils;
    }
    public void hideBackAndRematch() {
        homeButton.setVisible(false);
        rematchButton.setVisible(false);
    }
    public void showBackAndRematch() {
        homeButton.setVisible(true);
        rematchButton.setVisible(true);
    }

    public void after10Questions() {
        loadingAnimation();
        hideBackAndRematch();
    }
    public void after20Questions() {
        loadingLine.setVisible(false);
        fillWithValues();
        showBackAndRematch();
    }

    public void loadingAnimation() {
        loadingLine.setVisible(true);
        ScaleTransition st = new ScaleTransition(Duration.millis(5000), loadingLine);
        st.setToX(0);
        st.setOnFinished( t-> {
            Platform.runLater( () -> {
                appController.showNext();
            });
        });
        st.play();

    }

    public void fillWithValues() {
        int i = 0;
        int maxEntries = 8;
        System.out.println("Filling with values!");
        this.leaderboard = serverUtils.getLeaderboard(appController.getGameID());
        Game.printLeaderboardToScreen(this.leaderboard);
        Integer[] sortedScores = leaderboard.keySet().toArray(new Integer[0]);
        List<Integer> keysInt = new ArrayList<>();
        for (Integer integer : sortedScores) {
            if (leaderboard.get(integer).size() > 0) {
                keysInt.add(integer);
            }
        }
        sortedScores = keysInt.toArray(new Integer[0]);
        Arrays.sort(sortedScores, Collections.reverseOrder());
        boolean yourPaneNecessary = true;
        for (Integer score : sortedScores) {
            for (String name : leaderboard.get(score)) {
                if (i < maxEntries) {
                    names.get(i).setText(name);
                    scores.get(i).setText(score + "");
                    System.out.println();
                    if (name.equals(appController.getName())) {
                        yourPaneNecessary = false;
                        you_pane.setVisible(false);
                        names.get(i).setText(name);
                    }
                }
                else {
                    if (!yourPaneNecessary) {
                        break;
                    }
                    if (name.equals(appController.getName())) {
                        you_pane.setVisible(true);
                        your_rank.setText((i+1)+"");
                        your_score.setText(score+"");
                        break;
                    }
                }
                i++;
            }
        }
        if (i < maxEntries) {
            you_pane.setVisible(false);
            parentGridPane.setMinWidth(scrollPane.getWidth());
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            while (i < maxEntries) {
                panes.get(i).setVisible(false);
                i++;
            }
        }
    }


    public void goBack(){
        this.appController.initializeScore();
        this.appController.showHomeScreen();
    }

    public void rematch() {
        this.appController.initializeScore();
        this.appController.showNext();
        if(appController.isMultiPlayer()) {
            // show enter name scene
            HomeScreenMultiplayerCtrl ctrl = (HomeScreenMultiplayerCtrl) appController.getLinkedScene().getController();
            ctrl.setName(appController.getName());
            try {
                ctrl.enterRoom();
            } catch(InterruptedException e) {
                System.out.println(e);
            }
        } else {
            List<Question> questions = serverUtils.getRandomQuestions();
            appController.addQuestionScenes(questions, 1);
            this.appController.showNext();
        }
    }

    @Override
    public void initializeController() {
        appController.playSound("leaderboard");
        names = List.of(rank1_name, rank2_name, rank3_name, rank4_name, rank5_name, rank6_name, rank7_name, rank8_name);
        scores = List.of(rank1_score, rank2_score, rank3_score, rank4_score, rank5_score, rank6_score, rank7_score, rank8_score);
        panes = List.of(rank1_pane, rank2_pane, rank3_pane, rank4_pane, rank5_pane, rank6_pane, rank7_pane, rank8_pane);
        if (appController.isMultiPlayer()) {
            multiPlayerInitializer();
        }
        else {
            singlePlayerInitializer();
        }

    }

    public void disableRematch() {
        rematchButton.setDisable(true);
        rematchButton.setVisible(false);
    }

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

    public void singlePlayerInitializer() {
        you_pane.setVisible(false);
        List<Score> allScores = serverUtils.getSingleLeaderboard();
        System.out.println("ALL SCORES: " + allScores + allScores.size());
        int i;
        for (i = 0; i < 8; i++) {
            if (i < allScores.size()) {
                names.get(i).setText(allScores.get(i).getName());
                scores.get(i).setText(allScores.get(i).getScore()+"");
            }
            else {
                panes.get(i).setVisible(false);
            }
        }
        if (i < allScores.size()) {
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
    }

    public void multiPlayerInitializer() {
        fillWithValues();
        if (appController.getQuestionIndex() == 11) {
            after10Questions();
        }
        else {
            after20Questions();
        }
    }
}
