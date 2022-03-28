package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game = new Game(1);
    Player p = new Player("play");
    Player p1 = new Player("pl");
    @BeforeEach
    void setUp()
    {
        game.addPlayer(p);
        game.addPlayer(p1);
    }
    @Test
    void constructorTest() {assertNotNull(game);}
    @Test
    void getRequested() {
        assertEquals(game.getRequested(),0);
    }

    @Test
    void setRequested() {
        game.setRequested(5);
        assertEquals(game.getRequested(),5);
    }

    @Test
    void newRequest() {
        game.newRequest("option");
        assertEquals(game.getRequested(),1);
    }

    @Test
    void getGameID() {
        assertEquals(game.getGameID(),1);
    }

    @Test
    void setGameID() {
        game.setGameID(2);
        assertEquals(game.getGameID(),2);
    }

    @Test
    void getQuestions() {
        List<Question> ls = new ArrayList<>();
        Question q = new Question(new Activity(),new ArrayList<>(),QuestionType.EqualEnergy);
        ls.add(q);
        assertTrue(game.getQuestions().size()==0);
        game.setQuestions(ls);
        assertEquals(game.getQuestions(),ls);
    }

    @Test
    void setQuestions() {
        List<Question> ls = new ArrayList<>();
        Question q = new Question(new Activity(),new ArrayList<>(),QuestionType.EqualEnergy);
        Question q1 = new Question(new Activity(),new ArrayList<>(),QuestionType.Estimate);
        ls.add(q);
        ls.add(q1);
        game.setQuestions(ls);
        assertEquals(game.getQuestions(),ls);
        Question q2 = new Question(new Activity(),new ArrayList<>(),QuestionType.HighestEnergy);
        ls.add(q2);
        game.setQuestions(ls);
        assertEquals(game.getQuestions(),ls);
    }

    @Test
    void addPlayer() {
        assertTrue(game.getPlayers().contains(p));
        assertTrue(game.getPlayers().contains(p1));
    }

    @Test
    void removePlayer() {
        game.removePlayer(p1);
        assertFalse(game.getPlayers().contains(p1));
        assertTrue(game.getPlayers().contains(p));
    }

    @Test
    void getPlayers() {
        assertTrue(game.getPlayers().size()==2);
    }

    @Test
    void setPlayers() {
        Set<Player> st = new HashSet<Player>();
        Player player = new Player("new");
        st.add(player);
        game.setPlayers(st);
        assertEquals(game.getPlayers(),st);
    }

    @Test
    void getLeaderboard() {
        /*game.setScore(p1,50);
        game.setScore(p,100);
        List<Pair<Integer,Player>> leaderboard = game.getLeaderboard();
        assertTrue(leaderboard.get(0).getLeft()==100);
        assertTrue(leaderboard.get(1).getLeft()==50);
        assertTrue(leaderboard.get(1).getRight()==p1);
        assertTrue(leaderboard.get(0).getRight()==p); */
    }

    @Test
    void setScore() {
        /*game.setScore("abc",100);
        game.setScore("p1",75);
        assertEquals(game.getScore("abc"),100);
        assertNotEquals(game.getScore("p1"),100);*/

    }

    @Test
    void getScore() {
        /*game.setScore(p,100);
        game.setScore(p1,75);
        List<Pair<Integer,Player>>ls = new ArrayList<>();
        ls = game.getLeaderboard();
        assertEquals(ls.get(0).getLeft(),100);
        assertEquals(ls.get(1).getLeft(),75); */
    }

    @Test
    void getQuestion() {
        List<Question> ls = new ArrayList<>();
        Question q = new Question(new Activity(),new ArrayList<>(),QuestionType.EqualEnergy);
        Question q1 = new Question(new Activity(),new ArrayList<>(),QuestionType.Estimate);
        ls.add(q);
        ls.add(q1);
        game.setQuestions(ls);

        assertEquals(game.getQuestion(),q);
        game.IncrementQNum();
        assertEquals(game.getQuestion(),q1);
    }
}