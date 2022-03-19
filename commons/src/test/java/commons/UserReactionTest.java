package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserReactionTest {

    UserReaction ur;
    @BeforeEach
    void setUp() {
        ur = new UserReaction(123, "Angela", "angry");
    }

    @Test
    void getUsernameTest() {
        assertEquals("Angela", ur.getUsername());
    }

    @Test
    void setUsernameTest() {
        ur.setUsername("Paul");
        assertEquals("Paul", ur.getUsername());
    }

    @Test
    void getReactionTest() {
        assertEquals("angry", ur.getReaction());
    }

    @Test
    void setReaction() {
        ur.setReaction("happy");
        assertEquals("happy", ur.getReaction());
    }

    @Test
    void illegalReaction() {
        assertThrows(IllegalArgumentException.class, () -> ur.setReaction("bored"));
    }
}