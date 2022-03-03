import javax.persistence.Entity;
import java.util.List;


@Entity
public class Question{

    private final List<Activity> CHOICES;
    private final String TYPE;
    private final Activity CORRECT;


    public Question(Activity correct, List<Activity> choices, String type) {
        this.TYPE = type;
        this.CHOICES = choices;
        CHOICES.sort(new ActivityComparator());
        this.CORRECT = correct;
    }

    public List<Activity> getActivities() {
        return this.CHOICES;
    }

    public String getType() {
        return this.TYPE;
    }

    public Activity getCorrect() {
        return this.CORRECT;
    }

    public Boolean isCorrect() {
        return this.equals(CORRECT);
    }
}
