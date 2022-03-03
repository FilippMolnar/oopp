package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemplateActivityTest {
    TemplateActivity temp = new TemplateActivity("Name", 100 , "Source");
    @Test
    void ConstructorTest() {
        assertNotNull(temp);
    }

    @Test
    void getTitle() {
        assertEquals(temp.getTitle(),"Name");
    }

    @Test
    void setTitle() {
        temp.setTitle("NewName");
        assertEquals(temp.getTitle(),"NewName");
    }

    @Test
    void getConsumption_in_wh() {
        assertEquals(temp.getConsumption_in_wh(),100);
    }

    @Test
    void setConsumption_in_wh() {
        temp.setConsumption_in_wh(200);
        assertEquals(temp.getConsumption_in_wh(),200);
    }

    @Test
    void testEquals() {
        TemplateActivity eq = new TemplateActivity("Name" , 100 , "Source");
        TemplateActivity neq = new TemplateActivity("Name" , 100 , "Sourcee");
        assertTrue(temp.equals(eq));
        assertFalse(temp.equals(neq));
    }
}