package server.api;

import commons.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class QuestionController {

    private static ActivityController activityController;
    public QuestionController(ActivityController activityController){this.activityController=activityController;}
    @GetMapping("/question")
    public Question getRandomQuestion() {
//        int pick = new Random().nextInt(3);
//        if (pick == 0)
//            return getTypeEstimate();
//        else if (pick == 1)
//            return getTypeEqual();
//        else
            return getTypeMostLeast();
    }

    /**
     * Fetches data and constructs a question of type estimate
     *
     * @return question of type estimate    !!!    CHOICES ARE AN EMPTY LIST  !!!
     */
    @GetMapping(path = {"/estimate"})
    public Question getTypeEstimate() {
        Activity act = activityController.getRandom();
        Question q = new Question();
        q.setChoices(new ArrayList<>());
        q.setType(QuestionType.Estimate);
        q.setCorrect(act);

        return q;
    }

    /**
     * Fetches data and constructs a question of type most/least
     *
     * @return question of type most/least  !!!     CORRECT ANSWER IS NOT SET   !!!
     */
    @GetMapping(path = {"/most"})
    public Question getTypeMostLeast() {
        List<Activity> choices = new ArrayList<>();
        while (choices.size() < 3) {
            Activity act = activityController.getRandom();
            if (choices.contains(act)) continue;
            choices.add(act);
        }
        Activity highest = choices.get(0);
        for(Activity a : choices){
            highest = highest.getConsumption() > a.getConsumption() ? highest : a;
        }

        Question q = new Question();
        q.setCorrect(highest);
        q.setType(QuestionType.HighestEnergy);
        q.setChoices(choices);
        return q;
    }

    /**
     * Fetches data and constructs a question of type equal
     *
     * @return question of type equal  !!!    OPTIMIZED VERSION   !!!
     */
    @GetMapping(path = {"/equal"})
    public Question getTypeEqual() {
        Activity act = activityController.getRandom();
        List<Activity> same = activityController.getAllByConsumption(act.getConsumption());
        List<Activity> choices = new ArrayList<>();
        Activity neither = new Activity("neither", -1, "location of cross");

        if (same.size() == 1) same.add(neither);

        int idx = (int) (Math.random() * same.size());
        if (same.get(idx).equals(act)) {
            idx++;
            idx %= (same.size());
        }
        Question q = new Question();
        q.setCorrect(same.get(idx));

        choices.add(act);
        choices.add(same.get(idx));

        if(!choices.contains(neither))
        {
            int chance = (int)(Math.random()*100);
            if(chance <= 33)
            {
                choices.add(neither);
            }
        }

        while (choices.size() < 4) {
            Activity choice = activityController.getRandom();
            if(same.contains(choice)||act.equals(choice)||choices.contains(choice))continue;
            choices.add(choice);
        }
        q.setType(QuestionType.EqualEnergy);
        q.setChoices(choices);
        return q;
    }

}
