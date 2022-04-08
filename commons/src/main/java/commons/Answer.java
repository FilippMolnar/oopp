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

    public Answer(){}

    /**
     * Answer Constructor
     * @param isCorrect - is this answer correct
     * @param option - option A, B, or C that user has chosen
     * @param gameID -  game ID
     * @param score - score
     * @param username - user name
     */
    public Answer(boolean isCorrect, String option, int gameID, int score, String username) {
        this.isCorrect = isCorrect;
        this.option = option;
        this.gameID = gameID;
        this.username = username;
        this.score = score;
    }


    /**
     * Answer Constructor
     * @param isCorrect - is this answer correct
     * @param option - option A, B, or C that user has chosen
     */
    public Answer(boolean isCorrect, String option) {
        this.isCorrect = isCorrect;
        this.option = option;
        this.gameID = -1;
        this.score = 0;
        this.username = "";
    }

    /**
     * Compaeres if o is equal to this instance
     * @param o
     * @return
     */
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

    /**
     * @return Human-readable format of this Class
     */
    @Override
    public String toString() {
        return "Answer{" +
                "isCorrect=" + isCorrect +
                ", option='" + option + '\'' +
                ", score=" + score +
                ", username=" + username +
                '}';
    }


    /**
     * Getter for username
     * @return
     */
    public String getName() {
        return this.username;
    }

    /**
     * Setter for username
     * @param name - name to set
     */
    public void setName(String name) {
        this.username = name;
    }
}
