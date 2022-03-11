package server.api;

import commons.Game;
import commons.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    private static Map<Integer, Game> games = new HashMap<>();

    public GameController() {}

    public void addNewGame(int gameID, List<Player> players)
    {
        games.put(gameID,new Game(gameID,players));
    }

    public static Game getGame(int gameID)
    {
        return games.get(gameID);
    }
}
