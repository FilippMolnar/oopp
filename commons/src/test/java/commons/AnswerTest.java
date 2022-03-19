package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    Answer answer = new Answer(false,"option");
    Answer ans = new Answer(true,"option2");
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
        assertEquals(answer.toString(),"Answer{isCorrect=false, option='option'}");
    }

    @Test
    void testEquals() {
        assertTrue(answer.equals(new Answer(false,"option")));
    }
}