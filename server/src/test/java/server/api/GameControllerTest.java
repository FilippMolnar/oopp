package server.api;

import commons.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController gameController = new GameController(null, null);
    Player p1 = new Player("First");
    Player p2 = new Player("Second");

    @BeforeEach
    void setup() {
        gameController.addNewGame(1);
        p1.setSocketID("id1");
        p2.setSocketID("id2");

        gameController.addPlayerToGame(1,p2);
    }
    @Test
    void addNewGame() {
        assertNotNull(gameController.getGame(1));
    }

    @Test
    void addPlayerToGame() {
        gameController.addPlayerToGame(1,p1);
        Game game = gameController.getGame(1);
        Set<Player> s = game.getPlayers();
        assertTrue(s.contains(p1));
    }

    @Test
    void setScore() {
        gameController.addPlayerToGame(1,p1);
        gameController.setScore(1,p1,50);
        Game game = gameController.getGame(1);
        assertTrue(game.getScore(p1)==50);
    }

    @Test
    void removePlayer() {
        gameController.removePlayer(1,p2);
        gameController.addPlayerToGame(1,p1);
        Game game = gameController.getGame(1);
        assertFalse(game.getPlayers().contains(p2));
        gameController.removePlayer(1,p1);
        game = gameController.getGame(1);
        assertNull(game.getPlayers());
    }

    @Test
    void getGame() {
        assertNull(gameController.getGame(2));
        assertNotNull(gameController.getGame(1));
    }

    @Test
    void getLeaderboard() {
        gameController.setScore(1,p2,50);
        gameController.addPlayerToGame(1,p1);
        gameController.setScore(1,p1,10);
        List<Pair<Integer,Player>> leaderboard = gameController.getLeaderboard(1);
        assertEquals(2, leaderboard.size());
        assertEquals(leaderboard.get(0).getLeft(),50);
        assertEquals(leaderboard.get(0).getRight(), p2);
        assertEquals(leaderboard.get(1).getLeft(),10);
        assertEquals(leaderboard.get(1).getRight(),p1);
    }

    @Test
    void testSetScore() {
        Pair<Player,Integer> pl = Pair.of(p2,50);
        gameController.setScore(1,pl);
        Game game = gameController.getGame(1);
        assertEquals(game.getScore(p2),50);
    }

    @Test
    void getGameMapping() {
        assertNotNull(gameController.getGameMapping(1));
    }

    @Test
    void testGetGameQuestions()
    {
        Question q = new Question(new Activity(),new ArrayList<>(), QuestionType.Estimate);
        Game game = gameController.getGame(1);
        List<Question> ls = new ArrayList<>();
        ls.add(q);
        game.setQuestions(ls);
        assertEquals(ls,gameController.getGameQuestions(1));
    }
}
