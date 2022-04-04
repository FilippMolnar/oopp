package commons;

import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Data
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

    public Boolean isCorrect(Activity a) {
        return a.equals(correct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return choices.equals(question.choices) && type == question.type && correct.equals(question.correct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(choices, type, correct);
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
