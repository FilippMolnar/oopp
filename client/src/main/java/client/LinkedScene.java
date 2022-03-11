package client;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.Scene;

public class LinkedScene {

    private Scene current;
    private List<LinkedScene> next;
    private String title;

    public LinkedScene(Scene current) {
        this.current = current;
        this.title = null;
        this.next = new ArrayList();
    }

    public LinkedScene(Scene current, String title) {
        this.current = current;
        this.title = title;
        this.next = new ArrayList();
    }

    public LinkedScene(Scene current, List<LinkedScene> next) {
        this.title = null;
        this.current = current;
        this.next = next;
    }

    public LinkedScene(Scene current, String title, List<LinkedScene> next) {
        this.title = title;
        this.current = current;
        this.next = next;
    }

    public LinkedScene getNext() {
        return this.next.get(0);
    }

    public LinkedScene getNext(int i) {
        return this.next.get(i);
    }

    public void addNext(LinkedScene next) {
        this.next.add(next);
    }

    public Scene getScene() {
        return this.current;
    }

    public String getTitle() {
        return this.title;
    }

}
