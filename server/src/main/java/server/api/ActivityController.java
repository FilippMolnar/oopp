package server.api;

import commons.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api")
public class ActivityController {
    private static ActivityRepository activities;

    public ActivityController(ActivityRepository act)
    {
        activities = act;
    }

    public static boolean addActivity(Activity act)
    {
        if(act == null) return false;
        activities.save(act);
        return true;
    }

    @GetMapping(path = "/data/all")
    public List<Activity> getAllActivities() {
        System.out.println("get all");
        List<Activity> a = activities.findAll();
        Collections.sort(a);
        return a;
    }

    @GetMapping(path = "/data/rand")
    public Activity getRandom()
    {
        long size = activities.count();
        int idx = (int)(Math.random()*size);

        return activities.findAll().get(idx);
    }

    @GetMapping(path = "/data/rand_range")
    public List<Activity> getThreeRandom()
    {
        List<Activity> act = activities.findAll();
        Collections.sort(act);
        int seed = (int)(Math.random()*act.size());

        List<Activity> filtered = new ArrayList<>();
        filtered.add(act.get(seed));
        while(filtered.size() < 3){
            int rand = (int)(Math.random()*20);
            Activity act_to_add = act.get(0);
            if(seed-10+rand < 0)
                act_to_add = act.get(0);
            else if(seed-10+rand >= act.size())
                act_to_add = act.get(act.size()-1);
            else
                act_to_add = act.get(seed-10+rand);
            if(!filtered.contains(act_to_add)){
                filtered.add(act_to_add);
            }
        }
        return filtered;
    }

    @GetMapping(path = "/data/fetch/{cons}")
    public List<Activity> getAllByConsumption(@PathVariable("cons")int cons)
    {
        return activities.getByConsumption(cons, 100);
    }

    @GetMapping(path = "/data/diff/{cons}")
    public List<Activity>getAllDiffCons(@PathVariable("cons")int cons)
    {
        return activities.getAllDiff(cons, 100);
    }
}
