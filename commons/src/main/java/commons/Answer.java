package commons;

import java.util.Objects;

public class Answer {
    private boolean isCorrect;
    private String option;
    private int gameID;
    private int score;
    private String name;
    public Answer() { }
    public Answer(boolean isCorrect, String option, int gameID, int score, String username) {
        this.isCorrect = isCorrect;
        this.option = option;
        this.gameID = gameID;
        this.name = username;
        this.score = score;
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
                ", score=" + score +
                ", username=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return isCorrect == answer.isCorrect && Objects.equals(option, answer.option);
    }

    public int getGameID() {
        return gameID;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }
}
