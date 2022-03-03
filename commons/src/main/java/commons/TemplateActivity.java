package commons;

import java.util.Objects;

public class TemplateActivity {
    public String title;
    public int consumption_in_wh;
    public String source;

    public TemplateActivity() {

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
        return consumption_in_wh == that.consumption_in_wh && Objects.equals(title, that.title) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, consumption_in_wh, title);
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
