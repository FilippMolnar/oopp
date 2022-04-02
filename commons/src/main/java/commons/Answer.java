package commons;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Answer {

    @EqualsAndHashCode.Include
    private boolean isCorrect;
    @EqualsAndHashCode.Include
    private String option;
    private int gameID;
    private int score;
    private String name;

    public Answer(boolean isCorrect, String option, int gameID, int score, String username) {
        this.isCorrect = isCorrect;
        this.option = option;
        this.gameID = gameID;
        this.name = username;
        this.score = score;
    }
    public Answer(boolean isCorrect, String option) {
        this.isCorrect = isCorrect;
        this.option = option;
        this.gameID = -1;
        this.score = 0;
        this.name = "";
    }


    @Override
    public String toString() {
        return "Answer{" +
                "isCorrect=" + isCorrect +
                ", option='" + option + '\'' +
                ", score=" + score +
                ", username=" + name +
                '}';
    }


    public String getUsername() {
        return this.name;
    }

    public void setUsername(String o) {
        this.name = o;
    }
}
