package commons;

import java.util.Objects;

public class Answer {
    private boolean isCorrect;
    private String option;
    public Answer(boolean isCorrect, String option) {
        this.isCorrect = isCorrect;
        this.option = option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getOption() {
        return option;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "isCorrect=" + isCorrect +
                ", option='" + option + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return isCorrect == answer.isCorrect && Objects.equals(option, answer.option);
    }

}
