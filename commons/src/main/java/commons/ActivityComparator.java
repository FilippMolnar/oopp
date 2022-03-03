package commons;

import javax.persistence.Entity;
import java.util.Comparator;

@Entity
public class ActivityComparator implements Comparator<Activity> {

    public int compare(Activity a1, Activity a2) {
        return a1.compareTo(a2);
    }

}
