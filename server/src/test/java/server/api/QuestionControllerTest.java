package server.api;

import commons.Activity;
import commons.Question;
import commons.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionControllerTest {
    private TestActivityRepository repo;
    private ActivityController acont;
    private QuestionController cont;
    Activity act1 = new Activity("Activity1",1000,"path");
    Activity act2 = new Activity("Activity2",1500,"path1");
    Activity act3 = new Activity("Activity3",2000,"path2");
    Activity act4 = new Activity("Activity4",2500,"path3");
    @BeforeEach
    void setUp() {
        repo = new TestActivityRepository();
        acont = new ActivityController(repo);
        cont = new QuestionController(acont);
    }

    @Test
    void getRandomQuestion() {

        acont.addActivity(act2);
        acont.addActivity(act3);
        acont.addActivity(act4);

        Question q = cont.getRandomQuestion();

        if(q.getType().equals(QuestionType.HighestEnergy))
        {
            assertTrue(q.getCorrect().equals(act4));
            assertTrue(q.getChoices().size()==3);
        }
        else if(q.getType()==QuestionType.Estimate)
        {
            assertTrue(q.getChoices().size()==0);
            assertTrue(q.getCorrect().equals(act1)||q.getCorrect().equals(act2)||q.getCorrect().equals(act3)||q.getCorrect().equals(act4));
        }
    }

    @Test
    void getTypeEstimate() {
        acont.addActivity(act1);
        Question q = cont.getTypeEstimate();
        assertTrue(q.getCorrect().equals(act1) && q.getType().equals(QuestionType.Estimate) && q.getChoices().size()==0);
    }

    @Test
    void getTypeMostLeast() {

        acont.addActivity(act2);
        acont.addActivity(act3);
        acont.addActivity(act4);

        Question q = cont.getTypeMostLeast();
        assertTrue(q.getType().equals(QuestionType.HighestEnergy) && q.getCorrect().equals(act4) && q.getChoices().size()==3);
    }

    /**
     * Can`t be tested currently
     */
    @Test
    void getTypeEqual() {
    }
}