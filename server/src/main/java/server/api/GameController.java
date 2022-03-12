package server.api;

import commons.Game;
import commons.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    private static Map<Integer, Game> games = new HashMap<>();

    public GameController() {}

    public static void addNewGame(int gameID, List<Player> players)
    {
        games.put(gameID,new Game(gameID,players));
    }

    public static void addPlayerToGame(int gameID, Player player)
    {
        games.get(gameID).addPlayer(player);
    }

    public static void removePlayer(int gameID,Player player)
    {
        games.get(gameID).removePlayer(player);
    }

    public static Game getGame(int gameID)
    {
        return games.get(gameID);
    }

    /**
     * Retrieve the leaderboard for the current game
     * @param gameID identifier for the current game
     * @return a map which contains every player and his score for this game
     */
    @GetMapping(path = "api/leaderboard/{gameID}")
    public Map<Player,Integer> getScoreboard(@PathVariable("gameID") int gameID)
    {
        Game cur = getGame(gameID);

        return cur.getScoreboard();
    }
}
