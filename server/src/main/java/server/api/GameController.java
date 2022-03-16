package server.api;

import commons.Game;
import commons.Player;
import commons.UserReaction;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private Map<Integer, Game> games = new HashMap<>();
    private final SimpMessageSendingOperations simpMessagingTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    public GameController(SimpMessageSendingOperations simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

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

    @MessageMapping("/reactions")
    public void userReact(@Payload UserReaction ur) {
        System.out.println("aaaaaa");
        int gameID = ur.getGameID();
        Game current = this.getGame(gameID);
        var playerList = current.getPlayers();
        for (Player player : playerList) {
            String playerID = player.getSocketID();
            LOGGER.info("Sending reaction "+ ur);
            simpMessagingTemplate.convertAndSendToUser(playerID, "queue/reactions", ur);
            LOGGER.info("Sent reaction event "+ur+" to "+player.getName());
        }
    }

}
