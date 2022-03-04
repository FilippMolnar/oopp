package commons;

import java.util.List;
import java.util.Comparator;

public class Question{

    private List<Activity> CHOICES;
    private String TYPE;
    private Activity CORRECT;

    public Question(){}
    public Question(Activity correct, List<Activity> choices, String type) {
        this.TYPE = type;
        this.CHOICES = choices;

        // sorts in decending order
        //CHOICES.sort(new ActivityComparator());
        CHOICES.sort(Comparator.naturalOrder());
        this.CORRECT = correct;
    }

    public void setCHOICES(List<Activity> CHOICES) {
        this.CHOICES = CHOICES;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public void setCORRECT(Activity CORRECT) {
        this.CORRECT = CORRECT;
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
