package commons;

import java.util.Objects;

/**
 * This class is needed in order to convert JSON files to it and then to the actual Activity one
 */

public class TemplateActivity {
    public String title;
    public int consumption_in_wh;
    public String source;

    public TemplateActivity() {

    }

    public TemplateActivity(String title, int consumption_in_wh, String source) {
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public void setConsumption_in_wh(int consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateActivity)) return false;
        TemplateActivity that = (TemplateActivity) o;
        return consumption_in_wh == that.consumption_in_wh && Objects.equals(title, that.title) && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, consumption_in_wh, source);
    }

    @Override
    public String toString() {
        return "TemplateActivity{" +
                "title='" + title + '\'' +
                ", consumption_in_wh=" + consumption_in_wh +
                ", title='" + title + '\'' +
                '}';
    }
}
