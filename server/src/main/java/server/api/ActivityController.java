package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class ActivityController {
    private static ActivityRepository activities;

    public ActivityController(ActivityRepository act) {
        activities = act;
    }

    public boolean addActivity(Activity act) {
        if(act == null) return false;
        activities.save(act);
        return true;
    }

    @PostMapping(path = "/activities")
    public ResponseEntity<Activity> addAct(@RequestBody Activity activity) {
        if (activity == null || activity.getTitle() == null
                || activity.getTitle().isEmpty() || activity.getConsumption() == 0 || activity.getConsumption() < 0 || activity.getImagePath() == null
                || activity.getImagePath().isEmpty() || activity.getSource() == null || activity.getSource().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Activity added = activities.save(activity);
        return ResponseEntity.ok(added);
    }

    @GetMapping(path = "/data/all")
    public List<Activity> getAllActivities() {
        System.out.println("get all");
        List<Activity> a = activities.findAll();
        Collections.sort(a);
        return a;
    }

    @GetMapping(path = "/data/rand")
    public Activity getRandom() {
        long size = activities.count();
        int idx = (int)(Math.random()*size);
        return activities.findAll().get(idx);
    }

    @GetMapping(path = "/data/fetch/{cons}/{range}")
    public List<Activity> getAllByConsumption(@PathVariable("cons")int cons,@PathVariable("range")int range) {
        return activities.getByConsumption(cons, range);
    }

    @GetMapping(path = "/data/rand_range")
    public List<Activity> getThreeRandomActivities() {
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
    public List<Activity> getAllByConsumption(@PathVariable("cons")int cons) {
        return activities.getByConsumption(cons, 100);
    }

    @GetMapping(path = "/data/diff/{cons}")
    public List<Activity>getAllDiffCons(@PathVariable("cons")int cons) {
        return activities.getAllDiff(cons, 100);
    }

    @PostMapping("/activities/delete")
    @Transactional
    public void deleteActivity(@RequestBody Activity activity) {
        List<Activity> list = activities.getByConsumption(activity.getConsumption(),0);
        for (Activity a : list) {
            if (a.equals(activity)) {
                activities.deleteById(a.id);
                break;
            }
        }
    }
}
