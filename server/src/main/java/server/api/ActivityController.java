package server.api;

import commons.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

import java.util.List;

@RestController
public class ActivityController {
    private static ActivityRepository activities;

    public ActivityController(ActivityRepository act)
    {
        this.activities = act;
    }

    public static boolean addActivity(Activity act)
    {
        if(act == null) return false;
        activities.save(act);
        return true;
    }

    public static List<Activity> getAllActivities()
    {
        return activities.findAll();
    }

    @GetMapping(path = "/data/rand")
    public static Activity getRandom()
    {
        long size = activities.count();
        int idx = (int)(Math.random()*size);

        return activities.findAll().get(idx);

    }

    @GetMapping(path = "/data/fetch/{cons}")
    public static List<Activity> getAllByConsumption(@PathVariable("cons")int cons)
    {
        List<Activity> ls = activities.getByConsumption(cons);
        return ls;
    }

    @GetMapping(path = "/data/diff/{cons}")
    public static List<Activity>getAllDiffCons(@PathVariable("cons")int cons)
    {
        List<Activity> ls = activities.getAllDiff(cons);
        return ls;
    }
}
