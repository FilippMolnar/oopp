package commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  TO BE TESTED
 */
public class Game {
    private int gameID;
    private int size;
    private List<Question> questions = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Map<Player,int> scoreboard = new HashMap<>();
    private int count = 0; // Keeps track how many players have requested a new question

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

    public void IncrementCount() {
        this.count++;
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

    public Map<Player, int> getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Map<Player, int> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setScore(Player player,int score)
    {
        this.scoreboard.replace(player,score);
    }
}
