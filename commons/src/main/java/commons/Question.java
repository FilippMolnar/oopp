package commons;

import java.util.List;
import java.util.Comparator;

public class Question{

    private final List<Activity> CHOICES;
    private final String TYPE;
    private final Activity CORRECT;


    public Question(Activity correct, List<Activity> choices, String type) {
        this.TYPE = type;
        this.CHOICES = choices;

        // sorts in decending order
        //CHOICES.sort(new ActivityComparator());
        CHOICES.sort(Comparator.naturalOrder());
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

    public Boolean isCorrect(Activity a) {
        return a.equals(CORRECT);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Question type: ");
        str.append(TYPE);
        str.append("\nCorrect answer:\n");
        str.append(CORRECT.toString());
        str.append("\nOptions:\n");
        for(Activity choice : CHOICES) {
            str.append(choice.toString());
            str.append("\n");
        }
        return str.toString();
    }
}
