package commons;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 *  TO BE TESTED
 */
public class Game {
    private int gameID;
    private List<Question> questions = new ArrayList<>();
    private Set<Player> players = new HashSet<>();
    private List<Integer> scoreboard = new ArrayList<>(); // A list of the scores according to playerID
    private Map<Player, Integer> playerToID = new HashMap<>(); // Maps a player to his id
    private Map<Integer, Player> idToPlayer = new HashMap<>(); // Maps a playerID to player
    private Map<Integer , Boolean> inGame = new HashMap<>(); // Keeps track if player with id is in game
    private int requested = 0; // Keeps track of how many players have requested a new question
    private int qnum = 0 ; // Keeps track on which question we are throughout the game
    private int pnum = 0 ; // Keeps track of what the next player`s id should be
    private int pInGame = 0 ; // Keeps track of how many players are in current game

    public Game(){}

    public Game(int gameID) {
        this.gameID = gameID;
    }

    public int getRequested() {
        return requested;
    }

    public void setRequested(int count) {
        this.requested = count;
    }

    /**
     * Increments the number of players that have requested a new question
     * @return true if all players have requested a new question, false otherwise
     */
    public boolean newRequest() {
        this.requested++;
        if(this.requested == pInGame)
        {
            this.requested = 0;
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

    public void addPlayer(Player player)
    {
        inGame.put(pnum,true);
        pInGame++;
        players.add(player);
        scoreboard.add(0);
        playerToID.put(player,pnum);
        idToPlayer.put(pnum,player);
        pnum++;
    }

    /**
     * TODO optimize if necessary
     * Currently is set to only make the player 'disabled'
     * @param player to be removed
     */
    public void removePlayer(Player player)
    {
        players.remove(player);
        inGame.replace(playerToID.get(player),false);
        pInGame--;
    }

    /**
     * Get all players who have played at some point in time
     * @return a list of all players who have played
     */
    public Set<Player> getPlayers() {
        if(players.size()==0)return null;
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    /**
     * Retrieves a sorted scoreboard
     * @return a list of pairs of score and player in descending order
     */

    public List<Pair<Integer,Player>> getLeaderboard() {

        List<Pair<Integer,Player>> scores = new ArrayList<>();
        for(int i=0;i<pnum;i++)
        {
            if(inGame.get(i) == false)continue; // Players which left the game won`t be in the scoreboard
            Pair<Integer,Player> cscore =Pair.of(scoreboard.get(i),idToPlayer.get(i));
            scores.add(cscore);
        }
        Collections.sort(scores, new Comparator<Pair<Integer,Player>>(){
            @Override
            public int compare(final Pair<Integer,Player> p1 , final Pair<Integer,Player> p2)
            {
                if(p1.getKey()<p2.getKey())return 1;
                else return -1;
            }
        });
        return scores;
    }

    public void setScoreboard(List<Integer> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setScore(Player player,int score)
    {
        int id = playerToID.get(player);
        scoreboard.set(id,score);
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
