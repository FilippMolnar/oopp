package commons;


import java.util.*;

/**
 * TO BE TESTED
 */
public class Game {
    private final Map<Player, Integer> playerToID = new HashMap<>(); // Maps a player to his id
    private final Map<Integer, Player> idToPlayer = new HashMap<>(); // Maps a playerID to player
    private final Map<Integer, Boolean> inGame = new HashMap<>(); // Keeps track if player with id is in game
    private int gameID;
    private List<Question> questions = new ArrayList<>();
    private Set<Player> players = new HashSet<>();
    private int requested = 0; // Keeps track of how many players have requested a new question
    private int qnum = 0; // Keeps track on which question we are throughout the game
    private int pnum = 0; // Keeps track of what the next player`s id should be
    private Map<String, Integer> optionsStatistics = new TreeMap<>();
    private Map<String, Integer> players_index; // For quickly finding the score of a specific user
    private Map<Integer, List<String>> scores_index; // For quickly sorting the scores.

    public Game() {
        optionsStatistics.put("optionA", 0);
        optionsStatistics.put("optionB", 0);
        optionsStatistics.put("optionC", 0);
        this.scores_index = new HashMap<>();
        this.players_index = new HashMap<>();
    }

    public Game(int gameID) {
        this.gameID = gameID;
        optionsStatistics.put("optionA",0);
        optionsStatistics.put("optionB",0);
        optionsStatistics.put("optionC",0);
        this.scores_index = new HashMap<>();
        this.players_index = new HashMap<>();
    }

    public int getRequested() {
        return requested;
    }

    public void setRequested(int count) {
        this.requested = count;
    }

    public int getPlayersInGame() {
        return players.size();
    }

    /**
     * Increments the number of players that have requested a new question
     *
     * @return true if all players have requested a new question, false otherwise
     */
    public boolean newRequest(String option) {
        this.requested++;
        if (option.length() > 0) {
            int before = optionsStatistics.getOrDefault(option, 0);
            optionsStatistics.put(option, before + 1);
        }

        if (this.requested == players.size()) {
            this.requested = 0;
            return true;
        }
        return false;
    }

    public void resetOptions() {
        optionsStatistics.clear();
        optionsStatistics.put("optionA", 0);
        optionsStatistics.put("optionB", 0);
        optionsStatistics.put("optionC", 0);
    }

    public List<Integer> getOptionsStatistics() {
        return optionsStatistics.values().stream().toList();
    }

    public void IncrementQNum() {
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

    public void addPlayer(Player player) {
        inGame.put(pnum, true);
        players.add(player);
        if (this.scores_index.get(0) == null) {
            List lst = new ArrayList<>();
            lst.add(player.getName());
            this.scores_index.put(0, lst);
        } else {
            this.scores_index.get(0).add(player.getName());
        }
        this.players_index.put(player.getName(), 0);
        for (String s : this.players_index.keySet()) {
            System.out.println(this.players_index.get(s));
        }
        for (Integer score : this.scores_index.keySet()) {
            for (String name : this.scores_index.get(score)) {
                System.out.println(name + " - "+ score);
            }
        }
        System.out.println("----------------------------------");
    }

    /**
     * TODO optimize if necessary
     * Currently is set to only make the player 'disabled'
     *
     * @param player to be removed
     */

    public void removePlayer(Player player) {
        players.remove(player);
        inGame.replace(playerToID.get(player), false);
    }

    /**
     * Get all players who have played at some point in time
     *
     * @return a list of all players who have played
     */
    public Set<Player> getPlayers() {
        if (players.size() == 0) return null;
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    /**
     * Retrieves a sorted scoreboard
     *
     * @return a list of pairs of score and player in descending order
     */

    public Map<Integer, List<String>> getLeaderboard() {

        /*List<Pair<Integer, Player>> scores = new ArrayList<>();
        for (int i = 0; i < pnum; i++) {
            if (!inGame.get(i)) continue; // Players which left the game won`t be in the scoreboard
            Pair<Integer, Player> cscore = Pair.of(scoreboard.get(i), idToPlayer.get(i));
            scores.add(cscore);
        }
<<<<<<< HEAD
        scores.sort(Map.Entry.comparingByKey());
        return scores;
=======
        Collections.sort(scores, new Comparator<Pair<Integer, Player>>() {
            @Override
            public int compare(final Pair<Integer, Player> p1, final Pair<Integer, Player> p2) {
                if (p1.getKey() < p2.getKey()) return 1;
                else return -1;
            }
        });
        return scores;*/
        return this.scores_index;
    }

    public void updateScore(String name, int score) {
        /*int id = playerToID.get(player);
        scoreboard.set(id, score);*/
        int oldScore = this.players_index.get(name);
        int newScore = this.players_index.get(name) + score;
        this.players_index.put(name, newScore);
        if (this.scores_index.containsKey(newScore)) {
            this.scores_index.get(newScore).add(name);
        } else {
            List<String> lst = new ArrayList<>();
            lst.add(name);
            this.scores_index.put(newScore, lst);
        }
        this.scores_index.get(oldScore).remove(name);
    }

    public void setScore(String name, int score) {
        /*int id = playerToID.get(player);
        scoreboard.set(id, score);*/
        this.players_index.put(name, score);
        if (this.scores_index.containsKey(score)) {
            this.scores_index.get(score).add(name);
        } else {
            List<String> lst = new ArrayList<>();
            lst.add(name);
            this.scores_index.put(score, lst);
        }
    }

    public int getScore(Player player) {
        /*List<Pair<Integer, Player>> scores = getLeaderboard();
        int score = 0;
        for (var integerPlayerPair : scores) {
            Player p = integerPlayerPair.getRight();
            if (p.equals(player)) {
                score = integerPlayerPair.getLeft();
            }
        }
        return score;*/
        return this.players_index.get(player.getName());
    }

    /**
     * Retrieves the current question
     *
     * @return a question
     */
    public Question getQuestion() {
        return questions.get(this.qnum);
    }

    /**
     * For testing the leaderboard
     */
    public static void printLeaderboardToScreen(Map<Integer, List<String>> leaderboard) {
        Object[] keySet = leaderboard.keySet().toArray(new Object[0]);
        List<Integer> keysInt = new ArrayList<>();
        for (Object o : keySet) {
            System.out.println(o.getClass());
            if (o instanceof String) {
                keysInt.add(Integer.parseInt((String) o));
            }
            else {
                keysInt.add((Integer) o);
            }
        }
        Integer[] scores = keysInt.toArray(new Integer[0]);
        Arrays.sort(scores, Collections.reverseOrder());
        int i = 0;
        for(Integer score : scores) {
            for (String name : leaderboard.get(score)) {
                System.out.println((i+1)+") "+name+" - "+score);
            }
        }
    }
}
