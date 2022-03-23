package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Score{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private int score;

    private Score() {
        // for object mappers
    }

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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(name);
        str.append(": ");
        str.append(score);
        return str.toString();
    }
}
