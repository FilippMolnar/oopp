package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Activity implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private final String TITLE;
    private final String IMGPATH;

    // Consumption is in WH
    private final int CONSUMPTION;


    public Activity(String title, int consumption, String imgPath) {
        this.IMGPATH = imgPath;
        this.TITLE = title;
        this.CONSUMPTION = consumption;
    }

    public String getTitle() {
        return this.TITLE;
    }

    public String getImagePath() {
        return this.IMGPATH;
    }

    public int getConsumption() {
        return this.CONSUMPTION;
    }

    /*
     * Compares the consumption of two activities.
     * @parameters Activity to be compared to 'this'.
     * @return int -1 if this is greater than the input activity, 0 if both are equal
     * and 1 if the input activity is greater.
     */
    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Activity)) {
            throw new IllegalArgumentException();
        }
        Activity a = (Activity) o;
        if(a.CONSUMPTION > this.CONSUMPTION) {
            return 1;
        }
        return this.CONSUMPTION > a.CONSUMPTION ? -1 : 0;
    }

    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Activity)) return false;
        Activity that = (Activity) o;
        return this.TITLE.equals(that.TITLE)
            && this.CONSUMPTION == that.CONSUMPTION
            && this.IMGPATH == that.IMGPATH;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(TITLE);
        str.append(":\n");
        str.append(CONSUMPTION);
        str.append(" WH\n");
        str.append(IMGPATH);
        return str.toString();
    }

}
