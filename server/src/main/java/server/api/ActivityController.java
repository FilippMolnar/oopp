package server.api;

import commons.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

import java.util.List;

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

    public List<Activity> getAllActivities()
    {
        return activities.findAll();
    }

    @GetMapping(path = "/data/rand")
    public Activity getRandom()
    {
        long size = activities.count();
        int idx = (int)(Math.random()*size);

        return activities.findAll().get(idx);
    }

    @GetMapping(path = "/data/fetch/{cons}")
    public List<Activity> getAllByConsumption(@PathVariable("cons")int cons)
    {
        return activities.getByConsumption(cons,100);
    }

    @GetMapping(path = "/data/diff/{cons}")
    public List<Activity>getAllDiffCons(@PathVariable("cons")int cons)
    {
        return activities.getAllDiff(cons,100);
    }
}
