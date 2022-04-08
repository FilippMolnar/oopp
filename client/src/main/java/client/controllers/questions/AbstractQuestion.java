package client.controllers.questions;

import client.controllers.MainAppController;
import client.jokers.Joker;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;


public abstract class AbstractQuestion implements Initializable {
    protected final ServerUtils server;
    protected final MainAppController mainCtrl;
    protected Question question;

    @FXML
    protected Button optionA;
    @FXML
    protected Button optionB;
    @FXML
    protected Button optionC;
    @FXML
    protected Label countA;
    @FXML
    protected Label countB;
    @FXML
    protected Label countC;
    @FXML
    protected GridPane images;

    @FXML
    public GridPane parentGridPane;
    @FXML
    protected Circle circle1;
    @FXML
    protected Circle circle2;
    @FXML
    protected Circle circle3;
    @FXML
    protected ImageView image1;
    @FXML
    protected ImageView image2;
    @FXML
    protected ImageView image3;

    @FXML
    protected Arc timerArc;
    @FXML
    protected Text timerValue;

    @FXML
    protected Text questionNumber;

    protected boolean isMultiPlayer;

    @FXML
    protected Label informationLabel;

    @FXML
    protected Button splashButton;

    @FXML
    protected Label cons_A;
    @FXML
    protected Label cons_B;
    @FXML
    protected Label cons_C;


    private int timerIntegerValue;
    private static boolean google = false;
    protected int correctOption; // number of the correct option (0,1,2)
    protected int selectedOption; // number of the selected option (-1,0,1,2) (-1 if timer runs out)
    protected boolean hasSubmittedAnswer = false;


    public int getTimerIntegerValue() {
        return timerIntegerValue;
    }


    @FXML
    protected Text scoreText;

    protected Timeline timeline;

    TimerTask timerTask;
    Timer numberTimer;

    protected static boolean doublePointsJoker = false;

    @Inject
    public AbstractQuestion(ServerUtils server, MainAppController mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void resetChart() {
        List<Node> charts = images.lookupAll("Rectangle").stream().limit(3).toList();
        for (Node chart : charts) {
            var bar = (Rectangle) chart;
            bar.setVisible(false);
        }
    }

    public void showChart(List<Integer> ans, int correct) {
        List<Node> imageViews = images.lookupAll(".image-view").stream().limit(3).toList();
        List<Node> charts = images.lookupAll("Rectangle").stream().limit(3).toList();
        List<Label> labels = List.of(countA, countB, countC);
        for (int i = 0; i < labels.size(); i++) {
            if (ans.get(i) > 0) {
                Label label = labels.get(i);
                label.setVisible(true);
                label.setText("" + ans.get(i));
            }
        }

        System.out.println(ans.size());
        double all = ans.get(0) + ans.get(1) + ans.get(2);
        for (int i = 0; i < 3; i++) {
            imageViews.get(i).setVisible(false);
            double h = 150 * ans.get(i) / all;
            var bar = (Rectangle) charts.get(i);
            bar.setVisible(true);
            bar.setOpacity(1);
            if (i == correct)
                bar.setFill(Paint.valueOf("#95BF74"));
            else bar.setFill(Paint.valueOf("#C56659"));
            bar.setHeight(0);
            KeyValue heightValue = new KeyValue(bar.heightProperty(), bar.getHeight() + h);
            KeyFrame frame = new KeyFrame(Duration.millis(500), heightValue);
            Timeline timeline2 = new Timeline(frame);
            timeline2.play();
        }
    }

    public void displayAnswers(List<Integer> answerList) {
        if(!(mainCtrl.getCurrentScene().getController().getClass() == getClass())) {
            return;
        }
        List<Button> options = List.of(optionA,optionB,optionC);
        Button correctOption = options.get(this.correctOption);
        System.out.println("Correct option is : " + correctOption.getId());
        for(Button option : options){
            option.setDisable(true);
            option.setOpacity(1);
        }
        System.out.println("Received answer list: " + answerList + " from "
                + mainCtrl.getCurrentScene().getController().getClass() );
        if(isMultiPlayer) {
            showChart(answerList, this.correctOption);
        }
        Button selectedButton = null;
        if(selectedOption != -1) {
            selectedButton = options.get(selectedOption);
            selectedButton.setStyle("-fx-border-width: 2.4; -fx-border-color: #C56659");
        }

        correctOption.setStyle("-fx-border-width: 2.4; -fx-font-weight: bold; -fx-border-color: #83b159");
        Button finalSelectedButton = selectedButton;
        TimerTask delay = new TimerTask() {
            @Override
            public void run() {
                if(selectedOption != -1) {
                    finalSelectedButton.setStyle("-fx-border-width: 0; -fx-font-weight: normal;");
                }
                correctOption.setDisable(true);
                correctOption.setMouseTransparent(false);
                correctOption.setStyle("-fx-border-width: 0; -fx-font-weight: normal;");
                Platform.runLater(mainCtrl::showNext);
            }
        };
        Timer myTimer = new Timer();
        myTimer.schedule(delay, 3000); // wait for 3 seconds
        informationLabel.setVisible(true);
        informationLabel.setText("Stats received!");
        if (this instanceof QuestionSameAsCtrl q) {
            q.getCons().setText(question.getChoices().get(3).getConsumption()+" Wh");
        }
        cons_A.setText(question.getChoices().get(0).getConsumption()+" Wh");
        cons_B.setText(question.getChoices().get(1).getConsumption()+" Wh");
        cons_C.setText(question.getChoices().get(2).getConsumption()+" Wh");
    }

    public void setGameMode(boolean isMultiPlayer) {
        this.isMultiPlayer = isMultiPlayer;
    }

    public void setQuestion(Question question) {
        this.question = question;
        this.hasSubmittedAnswer = false;
        if (this instanceof QuestionMultiOptionsCtrl) {
            cons_A.setText("");
            cons_B.setText("");
            cons_C.setText("");
        }
    }

    public void setQuestionNumber(int num) {
        this.questionNumber.setText("Question " + num + "/20");
        if(num % 5 == 0) {
            mainCtrl.getJokers().replaceUsed(server, mainCtrl.isMultiPlayer());
            uncheckJokers();
        }
    }

    public void uncheckJokers(){
        getCircle1().setOpacity(1.0);
        getImage1().setOpacity(1.0);
        getCircle2().setOpacity(1.0);
        getImage2().setOpacity(1.0);
        getCircle3().setOpacity(1.0);
        getImage3().setOpacity(1.0);
    }

    public void showJokerImages(){
        System.out.println("show jokers");
        List<Joker> jokers = mainCtrl.getJokers().getJokers();
        var resources = getClass().getResource("/client/pictures/").toString();
        Image image1 = new Image(resources + jokers.get(0).imagePath);
        Image image2 = new Image(resources + jokers.get(1).imagePath);
        Image image3 = new Image(resources + jokers.get(2).imagePath);
        getImage1().setImage(image1);
        getImage2().setImage(image2);
        getImage3().setImage(image3);
        uncheckJokers();
        jokers.get(0).markUsed(mainCtrl);
        jokers.get(1).markUsed(mainCtrl);
        jokers.get(2).markUsed(mainCtrl);
    }

    public static void setDoublePointsJoker(boolean doublePointsJoker) {
        AbstractQuestion.doublePointsJoker = doublePointsJoker;
    }

    public void triggerJoker1() {
        mainCtrl.getJokers().getJokers().get(0).onClick(mainCtrl);
    }

    public void triggerJoker2() {
        mainCtrl.getJokers().getJokers().get(1).onClick(mainCtrl);
    }

    public void triggerJoker3() {
        mainCtrl.getJokers().getJokers().get(2).onClick(mainCtrl);
    }

    public void initialize(URL location, ResourceBundle resources) {
        server.subscribeForSocketMessages("/user/queue/reactions", UserReaction.class, userReaction -> userReaction(userReaction.getReaction(), userReaction.getUsername()));
    }

    public void userReaction(String reaction, String name) {
        if(!(mainCtrl.getCurrentScene().getController().getClass() == getClass())) {
            return;
        }
        Pane pane = new Pane();
        ImageView iv;
        Label label = new Label(name);
        String imagePath = "/client/pictures/" + reaction + ".png";
        System.out.println(imagePath);
        Image img = new Image(getClass().getResource(imagePath).toString());

        iv = new ImageView(img);
        pane.getChildren().add(iv);
        pane.getChildren().add(label);
        iv.setMouseTransparent(false);
        label.setMouseTransparent(false);
        label.setPadding(new Insets(-20, 0, 0, 5));
        TranslateTransition translate = new TranslateTransition();
        translate.setByY(700);
        double width = parentGridPane.getCellBounds(parentGridPane.getColumnCount() - 1, 0).getWidth() - 70;
        translate.setFromX(width);
        translate.setToX(width);
        translate.setDuration(Duration.millis(2800));
        translate.setNode(pane);
        translate.setOnFinished(t -> {
                    parentGridPane.getChildren().remove(pane);
                    pane.getChildren().remove(iv);
                    pane.getChildren().remove(label);
                }
        );
        translate.play();

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(2000));
        //fade.setDelay(Duration.millis(1000));
        fade.setFromValue(10);
        fade.setToValue(0);
        fade.setNode(pane);
        fade.play();
        parentGridPane.add(pane, parentGridPane.getColumnCount() - 1, 0);
    }

    public void angryReact() {
        String path = "/app/reactions";
        if(isMultiPlayer) {
            server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "angry"));
        }
        else {
            userReaction("angry",mainCtrl.getName());
        }
    }

    public void angelReact() {
        String path = "/app/reactions";
        if(isMultiPlayer) {
            server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "angel"));
        }
        else {
            userReaction("angel",mainCtrl.getName());
        }
    }

    public void happyReact() {
        String path = "/app/reactions";
        if(isMultiPlayer) {
            server.sendThroughSocket(path, new UserReaction(mainCtrl.getGameID(), mainCtrl.getName(), "happy"));
        }
        else {
            userReaction("happy",mainCtrl.getName());
        }
    }

    public void stopTimer() {
        timeline.stop();
        timerTask.cancel();
        numberTimer.cancel();
    }

    public void cutAnimationInHalf() {
        stopTimer();
        startTimerAnimation(timerIntegerValue / 2);

    }

    public void addTimeForGoogling()
    {
        google = true;
        stopTimer();
        startTimerAnimation(timerIntegerValue+10);
    }

    public void startTimerAnimation(int length) {
        timerIntegerValue = length;
        timerArc.setLength(360);
        timerArc.setFill(Paint.valueOf("#d6d3ee"));
        timerValue.setFill(Paint.valueOf("#d6d3ee"));
        //create a timeline for moving the circle
        this.timeline = new Timeline();
        //You can add a specific action when each frame is started.

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timerIntegerValue--;
                    if (timerIntegerValue < 0) {
                        timerValue.setText(Integer.toString(0));
                    } else {
                        timerValue.setText(Integer.toString(timerIntegerValue));
                    }
                    if (timerIntegerValue <= 3) {
                        timerArc.setFill(Paint.valueOf("red")); // set the color to red when the timer runs out
                    }
                });
            }
        };
        numberTimer = new Timer();
        timerValue.setText(Integer.toString(length));
        numberTimer.scheduleAtFixedRate(timerTask, 1000, 1000);

        //create a keyValue with factory: scaling the circle 2times
        KeyValue lengthProperty = new KeyValue(timerArc.lengthProperty(), 0);


        //create a keyFrame, the keyValue is reached at time 2s
        System.out.println(timerValue.getText());
        Duration duration = Duration.millis(length * 1000);

        EventHandler<ActionEvent> onFinished = t -> {
            System.out.println("animation finished!");
            numberTimer.cancel();
            timerIntegerValue = 0;
            timerValue.setText("0");
            if (!hasSubmittedAnswer) {
                disableOptions();
                if(isMultiPlayer) {
                    sendAnswer(new Answer(false, "", mainCtrl.getGameID(), 0, mainCtrl.getName()));
                } else {
                    displayAnswers(new ArrayList());
                }
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished, lengthProperty);

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void disableOptions() {
        if (mainCtrl.getCurrentScene().getController() instanceof QuestionMultiOptionsCtrl qCtrl) {
            qCtrl.getOptionA().setDisable(true);
            qCtrl.getOptionB().setDisable(true);
            qCtrl.getOptionC().setDisable(true);
        }
    }

    /**
     * send answer to the server
     *
     * @param answer true/false depending if the selected answer was good
     */
    public void sendAnswer(Answer answer) {
        informationLabel.setVisible(true);
        informationLabel.setText("Answer submitted!");
        hasSubmittedAnswer = true;
        stopTimer();
        server.sendThroughSocket("/app/submit_answer", answer);
    }

    public void checkAnswer(Answer answer) {
        int newScore = calculateScore(answer.isCorrect(), Double.parseDouble(timerValue.getText()));
        answer.setScore(newScore);
        mainCtrl.updateScore(newScore);
        scoreText.setText("SCORE " + mainCtrl.getScore());
    }

    public void backToHomeScreen() {
        stopTimer();
        Player player = new Player(mainCtrl.getName());
        player.setGameID(mainCtrl.getGameID());
        server.sendThroughSocket("/app/disconnectFromGame",player);
        mainCtrl.showHomeScreen();
    }
    // for single player
    public int calculateScore(boolean answerCorrect, double secondsLeft) {
        int scoreToBeAdded = 0;
        double maxSeconds = 10;
        int maxPoints = 100;
        double secondsToAnswer = maxSeconds - secondsLeft;
        if (answerCorrect) {
            scoreToBeAdded = (int) Math.round(maxPoints * (1 - ((secondsToAnswer / maxSeconds) / 1.5)));
        }
        return scoreToBeAdded;
    }

    public int calculateScoreGoogle(boolean answerCorrect, double secondsLeft) {

        int scoreToBeAdded = 0;
        double maxSeconds = 20;
        int maxPoints = 100;
        double secondsToAnswer = maxSeconds - secondsLeft;
        if (answerCorrect) {
            scoreToBeAdded = (int) Math.round(maxPoints * (1 - ((secondsToAnswer / maxSeconds) / 1.0)));
        }
        return scoreToBeAdded;
    }

    public void sendAnswerAndUpdateScore(MainAppController mainCtrl, String button_id, Activity a) {
        int score;
        if(google == false){score = calculateScore(a.id == question.getCorrect().id, 10.0 - (double) this.getTimerIntegerValue());}
        else {score = calculateScoreGoogle(a.id == question.getCorrect().id, 20.0 - (double) this.getTimerIntegerValue());}
        if (doublePointsJoker) score = score * 2;
        setDoublePointsJoker(false);
        mainCtrl.updateScore(score);
        this.scoreText.setText("SCORE " + mainCtrl.getScore());
        if (isMultiPlayer) {
            sendAnswer(new Answer(a.id == question.getCorrect().id, button_id, mainCtrl.getGameID(), score, mainCtrl.getName()));
        } else {
            checkAnswer(new Answer(a.id == question.getCorrect().id, button_id, 0, score, mainCtrl.getName()));
            System.out.println("Stopping timer");
            stopTimer();
            displayAnswers(new ArrayList());
        }
    }

    public Circle getCircle1() {
        return circle1;
    }

    public Circle getCircle2() {
        return circle2;
    }

    public Circle getCircle3() {
        return circle3;
    }

    public ImageView getImage1() { return image1; }

    public ImageView getImage2() {
        return image2;
    }

    public ImageView getImage3() {
        return image3;
    }
}
