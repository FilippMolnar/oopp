package server.api;

import commons.Game;
import commons.Player;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private Map<Integer, Game> games = new HashMap<>();

    public GameController() {}

    public void addNewGame(int gameID)
    {
        games.put(gameID,new Game(gameID));
    }

    public void addPlayerToGame(int gameID, Player player)
    {
        if(games.get(gameID) == null)
        {
            addNewGame(gameID);
        }
        games.get(gameID).addPlayer(player);
    }

    public void setScore(int gameID,Player player, int score)
    {
        games.get(gameID).setScore(player,score);
    }

    public void removePlayer(int gameID,Player player)
    {
        games.get(gameID).removePlayer(player);
    }

    public Game getGame(int gameID)
    {
        return games.get(gameID);
    }

    /**
     * Retrieve the leaderboard for the current game
     * @param gameID identifier for the current game
     * @return a list of pairs of score and player sorted in descending order by their score
     */
    @GetMapping(path = "api/leaderboard/{gameID}")
    public List<Pair<Integer,Player>> getLeaderboard(@PathVariable("gameID") int gameID)
    {
        Game cur = getGame(gameID);

        return cur.getLeaderboard();
    }
}
