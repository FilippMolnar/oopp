package server.api;

import commons.Game;
import commons.Player;
import commons.Question;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class GameController {

    private final Map<Integer, Game> games = new HashMap<>();

    private final Map<String , Game> socketIDToGame = new HashMap<>(); // Maps each socketID to its corresponding game

    public GameController() {
    }

    public void addNewGame(int gameID) {
        games.put(gameID, new Game(gameID));
    }

    public void addPlayerToGame(int gameID, Player player) {
        socketIDToGame.put(player.getSocketID(),games.get(gameID));
        player.setGameID(gameID);
        if (games.get(gameID) == null) {
            addNewGame(gameID);
        }
        games.get(gameID).addPlayer(player);
    }

    public void setScore(int gameID, Player player, int score) {
        games.get(gameID).setScore(player, score);
    }

    public void removePlayer(int gameID, Player player) {
        games.get(gameID).removePlayer(player);
    }

    public Game getGame(int gameID) {
        return games.get(gameID);
    }

    /**
     * Retrieve the leaderboard for the current game
     *
     * @param gameID identifier for the current game
     * @return a list of pairs of score and player sorted in descending order by their score
     */
    @GetMapping(path = "/game/leaderboard/{gameID}")
    public List<Pair<Integer, Player>> getLeaderboard(@PathVariable("gameID") int gameID) {
        Game cur = getGame(gameID);

        return cur.getLeaderboard();
    }

    @PostMapping(path = "/game/score/{gameID}")
    public void setScore(@PathVariable("gameID") int gameID, Pair<Player, Integer> pair) {
        Game cur = getGame(gameID);
        Player player = pair.getLeft();
        int score = pair.getRight();
        cur.setScore(player, score);
    }

    @GetMapping(path = "/game/getGame/{gameID}")
    public Game getGameMapping(@PathVariable("gameID") int gameID) {
        Game cur = getGame(gameID);
        return cur;
    }

    @GetMapping(path = "/game/getQuestions/{gameID}")
    public List<Question> getGameQuestions(@PathVariable("gameID") int gameID) {
        Game currentGame = getGame(gameID);
        System.out.println("Sending the questions " + currentGame.getQuestions());
        return currentGame.getQuestions();
    }
}
