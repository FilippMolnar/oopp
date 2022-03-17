package commons;

import javax.persistence.Entity;

@Entity
public class Score{
    private String name;
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score; 
    }

    public int getScore() {
        return this.score; 
    }

    public void setScore(int score) {
        this.score = score; 
    }

    public String getName() {
        return this.name; 
    }

    public void addScore(int toAdd) {
        this.score += toAdd; 
    }
}
