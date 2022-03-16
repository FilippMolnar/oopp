package server.api;

import commons.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionController {

    public QuestionController(){}
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
        Activity act = ActivityController.getRandom();
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
            Activity act = ActivityController.getRandom();
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
     * @return question of type equal  !!!    NEEDS OPTIMIZATION   !!!
     */
    @GetMapping(path = {"/equal"})
    public Question getTypeEqual() {
        Activity act = ActivityController.getRandom();
        List<Activity> same = ActivityController.getAllByConsumption(act.getConsumption());
        List<Activity> diff = ActivityController.getAllDiffCons(act.getConsumption());
        List<Activity> choices = new ArrayList<>();
        Activity neither = new Activity("neither", -1, "location of cross");

        if (same.size() == 1) same.add(neither);
        else diff.add(neither);

        int idx = (int) (Math.random() * same.size());
        if (same.get(idx).equals(act)) {
            idx++;
            idx %= (same.size());
        }
        Question q = new Question();
        q.setCorrect(same.get(idx));

        choices.add(act);
        choices.add(same.get(idx));

        while (choices.size() < 4) {
            idx = (int) (Math.random() * diff.size());
            if (choices.contains(diff.get(idx))) continue;
            choices.add(diff.get(idx));
        }
        q.setType(QuestionType.EqualEnergy);
        q.setChoices(choices);
        return q;
    }

}
