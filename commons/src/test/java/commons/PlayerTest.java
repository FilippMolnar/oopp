package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = new Player("player");
    @Test
    void getGameID() {
        assertEquals(player.getGameID(),0);
    }

    @Test
    void setGameID() {
        player.setGameID(5);
        assertEquals(player.getGameID(),5);
    }

    @Test
    void getName() {
        assertEquals(player.getName(),"player");
    }

    @Test
    void getSocketID() {
        assertNull(player.getSocketID());
    }

    @Test
    void setSocketID() {
        player.setSocketID("ID1");
        assertEquals(player.getSocketID(),"ID1");
    }

    @Test
    void equalsTest()
    {
        Player check = new Player("player");
        assertEquals(player, check);
    }

}