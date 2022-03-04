package server.api;

import commons.Activity;
import commons.Question;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionController {

    /**
     * Fetches activities and creates a question of type equal amount
     * @return question of the type equal amount
     */
    // Should make it Fetch only part of database, not everything ** OPTIMIZATION NEEDED **
    @GetMapping(path = {"/equal"})
    public static Question fetchTypeEqual() {
        Activity act = ActivityController.getRandom();
        List<Activity> same = ActivityController.getAllByConsumption(act.getConsumption());
        List<Activity> diff = ActivityController.getAllDiffCons(act.getConsumption());
        Activity neither = new Activity("neither", -1, "Location of cross");
        Question q = new Question();

        if (same.size() == 1) same.add(neither);
        else diff.add(neither);

        int idx = (int) (Math.random() * same.size());

        if (same.get(idx).equals(act)) {
            idx++;
            idx %= (same.size());
        }
        q.setCORRECT(same.get(idx));

        List<Activity> choices = new ArrayList<>();
        choices.add(same.get(idx));
        for (int i = 0; i < 2; i++) {
            idx = (int) (Math.random() * diff.size());
            choices.add(diff.get(idx));
        }
        q.setCHOICES(choices);
        q.setTYPE("Equal");
        System.out.println(act);
        return q;
    }

    /**
     * Fetches data and constructs a question of type most/least
     * @return question of the most/least type !!!   THE CORRECT ANSWER IS SET TO AN EMPTY ACTIVITY  !!!
     */
    @GetMapping(path = {"/most"})
    public static Question fetchTypeMostLeast()
    {
        Activity act = ActivityController.getRandom();
        List<Activity> choices = new ArrayList<>();

        choices.add(act);
        while(choices.size()<3)
        {
            act = ActivityController.getRandom();
            if(choices.contains(act))continue;
            choices.add(act);
        }

        Question q = new Question();
        q.setTYPE("Most/Least");
        q.setCHOICES(choices);
        q.setCORRECT(new Activity());
        return q;
    }

    /**
     * Fetches data and constructs a question of type estimate
     * @return question of the estimate type !!!    CHOICES ARE SET TO EMPTY LIST   !!!
     */
    @GetMapping(path = {"/estimate"})
    public static Question fetchTypeEstimate()
    {
        Activity act = ActivityController.getRandom();
        Question q = new Question();
        q.setTYPE("Estimate");
        q.setCORRECT(act);
        q.setCHOICES(new ArrayList<>());
        return q;
    }
}
