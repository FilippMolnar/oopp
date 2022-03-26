package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import javax.transaction.Transactional;
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

    @GetMapping(path = "/activities")
    public Iterable<Activity> getAllActivities()
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
        return activities.getByConsumption(cons, 100);
    }

    @GetMapping(path = "/data/diff/{cons}")
    public List<Activity>getAllDiffCons(@PathVariable("cons")int cons)
    {
        return activities.getAllDiff(cons, 100);
    }

    @PostMapping("/activities/del")
    @Transactional
    public ResponseEntity<Activity> deleteActivity(@RequestBody Activity activity) {
        List<Activity> list = activities.findAll();
        Activity candidate = null;
        //        Activity candidate = activities.findById(activity.id);
        for (Activity a : list) {
            if (a.equals(activity)) {
                candidate = a;
            }
        }
        if (candidate == null) {
            return ResponseEntity.badRequest().build();
        }
        activities.deleteById(activity.id);
        return ResponseEntity.ok(candidate);
    }
}
