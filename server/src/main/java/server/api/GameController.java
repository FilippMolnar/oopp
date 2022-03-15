package server.api;

import commons.Game;
import commons.Player;
import commons.Question;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private final Map<Integer, Game> games = new HashMap<>();

    public GameController() {
    }

    public void addNewGame(int gameID) {
        games.put(gameID, new Game(gameID));
    }

    public void addPlayerToGame(int gameID, Player player) {
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
    @GetMapping(path = "api/leaderboard/{gameID}")
    public List<Pair<Integer, Player>> getLeaderboard(@PathVariable("gameID") int gameID) {
        Game cur = getGame(gameID);

        return cur.getLeaderboard();
    }

    @PostMapping(path = "api/game/score/{gameID}")
    public void setScore(@PathVariable("gameID") int gameID, Pair<Player, Integer> pair) {
        Game cur = getGame(gameID);
        Player player = pair.getLeft();
        int score = pair.getRight();
        cur.setScore(player, score);
    }

    @GetMapping(path = "api/game/getGame/{gameID}")
    public Game getGameMapping(@PathVariable("gameID") int gameID) {
        Game cur = getGame(gameID);
        return cur;
    }

    @GetMapping("api/game/getQuestions/{gameID}")
    private List<Question> getGameQuestions(@PathVariable("gameID") int gameID) {
        Game currentGame = getGame(gameID);
        System.out.println("Sending the questions " + currentGame.getQuestions());
        return currentGame.getQuestions();
    }
}
