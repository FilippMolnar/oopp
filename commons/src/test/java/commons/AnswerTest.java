package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    Answer answer = new Answer(false,"option",1,100,"Ana");
    Answer ans = new Answer(true,"option2",1,75,"David");

    @Test
    void testConstructor2() {
        Answer a = new Answer(true,"option");
        assertTrue(a.isCorrect());
        assertEquals(a.getOption(),"option");

    }

    @Test
    void isCorrect() {
        assertFalse(answer.isCorrect());
        assertTrue(ans.isCorrect());
    }

    @Test
    void getOption() {
        assertEquals(answer.getOption(),"option");
        assertEquals(ans.getOption(),"option2");
    }

    @Test
    void testToString() {
        String s = "Answer{isCorrect=false, option='option', score=100, username=Ana}";
        assertEquals(answer.toString(),s);
    }


    @Test
    void testEquals() {
        assertTrue(ans.equals(ans));
        assertFalse(answer.equals(null));
        assertFalse(answer.equals("string"));
        assertTrue(answer.equals(new Answer(false,"option",1,100,"Ana")));
        assertFalse(answer.equals(new Answer(false,null,1,100,null)));
        assertFalse(answer.equals(ans));
    }

    @Test
    void testGetGameId() {
        assertEquals(answer.getGameID(),1);
        assertNotEquals(ans.getGameID(),2);
    }

    @Test
    void testSetCorrect() {
        Answer a = new Answer(false,"option",1,100,"Ana");
        assertFalse(a.isCorrect());
        a.setCorrect(true);
        assertTrue(a.isCorrect());
    }

    @Test
    void testSetOption() {
        Answer a = new Answer(false,"option",1,100,"Ana");
        assertEquals(a.getOption(), "option");
        a.setOption("o");
        assertNotEquals(a.getOption(), "option");
        assertEquals(a.getOption(), "o");
    }

    @Test
    void testSetGameId() {
        Answer a = new Answer(false,"option",1,100,"Ana");
        assertEquals(a.getGameID(), 1);
        a.setGameID(2);
        assertNotEquals(a.getGameID(), 1);
        assertEquals(a.getGameID(), 2);
    }

    @Test
    void testSetScore() {
        Answer a = new Answer(false,"option",1,100,"Ana");
        assertEquals(a.getScore(), 100);
        a.setScore(2);
        assertNotEquals(a.getScore(), 100);
        assertEquals(a.getScore(), 2);
    }

    @Test
    void testSetUsername() {
        Answer a = new Answer(false,"option",1,100,"Ana");
        assertEquals(a.getUsername(), "Ana");
        a.setUsername("o");
        assertNotEquals(a.getUsername(), "Ana");
        assertEquals(a.getUsername(), "o");
    }
}