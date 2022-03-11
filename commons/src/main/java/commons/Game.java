package commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  TO BE TESTED
 */
public class Game {
    private int gameID;
    private int size;
    private List<Question> questions = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Map<Player, Integer> scoreboard = new HashMap<>(); // Maps a player to his current score
    private int count = 0; // Keeps track of how many players have requested a new question
    private int qnum = 0 ; // Keeps track on which question we are throughout the game

    public Game(){}

    public Game(int gameID, List<Player> players) {
        this.gameID = gameID;
        this.players = players;
        this.size = players.size();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Increments the number of players that have requested a new question
     * @return true if all players have requested a new question, false otherwise
     */
    public boolean IncrementCount() {
        this.count++;
        if(this.count == this.size)
        {
            this.count = 0;
            return true;
        }
        return false;
    }

    public void IncrementQNum()
    {
        this.qnum++;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Map<Player, Integer> getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Map<Player, Integer> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setScore(Player player,int score)
    {
        this.scoreboard.replace(player,score);
    }

    /**
     * Retrieves the current question
     * @return a question
     */
    public Question getQuestion()
    {
        return questions.get(this.qnum);
    }
}
