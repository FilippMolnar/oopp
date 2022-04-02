package commons;

import lombok.Data;

import java.util.Objects;

@Data
public class Answer {

    private boolean isCorrect;
    private String option;
    private int gameID;
    private int score;
    private String username;

    public Answer(boolean isCorrect, String option, int gameID, int score, String username) {
        this.isCorrect = isCorrect;
        this.option = option;
        this.gameID = gameID;
        this.username = username;
        this.score = score;
    }
    public Answer(boolean isCorrect, String option) {
        this.isCorrect = isCorrect;
        this.option = option;
        this.gameID = -1;
        this.score = 0;
        this.username = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return isCorrect == answer.isCorrect && gameID == answer.gameID && score == answer.score && option.equals(answer.option) && username.equals(answer.username);
    }


    @Override
    public int hashCode() {
        return Objects.hash(isCorrect, option, gameID, score, username);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "isCorrect=" + isCorrect +
                ", option='" + option + '\'' +
                ", score=" + score +
                ", username=" + username +
                '}';
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String o) {
        this.username = o;
    }
}
