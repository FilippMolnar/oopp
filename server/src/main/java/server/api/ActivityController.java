package server.api;

import commons.Activity;
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

    public static Activity getRandom()
    {
        long size = activities.count();
        int idx = (int)(Math.random()*size);

        return activities.findAll().get(idx);
    }
}
