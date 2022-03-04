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
     * Fetches data and constructs a question of type estimate
     * @return question of type estimate    !!!    CHOICES ARE AN AEMPTY LIST  !!!
     */
    @GetMapping(path = {"/estimate"})
    public static Question getTypeEstimate()
    {
        Activity act = ActivityController.getRandom();
        Question q = new Question();
        q.setCHOICES(new ArrayList<>());
        q.setTYPE("Estimate");
        q.setCORRECT(act);

        return q;
    }

    /**
     * Fetches data and constructs a question of type most/least
     * @return question of type most/least  !!!     CORRECT ANSWER IS NOT SET   !!!
     */
    @GetMapping(path = {"/most"})
    public static Question getTypeMostLeast()
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
        q.setCORRECT(new Activity());
        q.setTYPE("Most/Least");
        q.setCHOICES(choices);
        return q;
    }

    /**
     * Fetches data and constructs a question of type equal
     * @return question of type equal  !!!    NEEDS OPTIMIZATION   !!!
     */
    @GetMapping(path = {"/equal"})
    public static Question getTypeEqual()
    {
        Activity act = ActivityController.getRandom();
        List<Activity> same = ActivityController.getAllByConsumption(act.getConsumption());
        List<Activity> diff = ActivityController.getAllDiffCons(act.getConsumption());
        List<Activity> choices = new ArrayList<>();
        Activity neither = new Activity("neither",-1,"location of cross");

        if(same.size()==1)same.add(neither);
        else diff.add(neither);

        int idx = (int)(Math.random()*same.size());
        if(same.get(idx).equals(act))
        {
            idx++;
            idx%=(same.size());
        }
        Question q = new Question();
        q.setCORRECT(same.get(idx));

        choices.add(act);
        choices.add(same.get(idx));

        while(choices.size()<4)
        {
            idx = (int)(Math.random()*diff.size());
            if(choices.contains(diff.get(idx)))continue;
            choices.add(diff.get(idx));
        }

        q.setTYPE("Equal");
        q.setCHOICES(choices);
        return q;
    }
}
