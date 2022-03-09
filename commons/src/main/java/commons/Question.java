package commons;

import java.util.List;
import java.util.Comparator;

public class Question{

    private List<Activity> choices;
    private QuestionType type;
    private Activity correct;

    public Question(){

    }

    public Question(Activity correct, List<Activity> choices, QuestionType type) {
        this.type = type;
        this.choices = choices;
        this.correct = correct;

        // sorts in decending order
        //CHOICES.sort(new ActivityComparator());
        this.choices.sort(Comparator.naturalOrder());
    }

    public List<Activity> getChoices() {
        return this.choices;
    }

    public QuestionType getType() {
        return type;
    }

    public Activity getCorrect() {
        return correct;
    }

    public void setChoices(List<Activity> choices) {
        this.choices = choices;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public void setCorrect(Activity correct) {
        this.correct = correct;
    }

    public Boolean isCorrect(Activity a) {
        return a.equals(correct);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Question type: ");
        str.append(type);
        str.append("\nCorrect answer:\n");
        str.append(correct.toString());
        str.append("\nOptions:\n");
        for(Activity choice : choices) {
            str.append(choice.toString());
            str.append("\n");
        }
        return str.toString();
    }
}
