package client;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class LinkedScene {

    private final Scene current;
    private final List<LinkedScene> next;
    private final String title;
    private Object controller;


    public LinkedScene(Scene current) {
        this.current = current;
        this.title = null;
        this.next = new ArrayList<>();
    }

    public LinkedScene(Scene current, Object controller) {
        this.current = current;
        this.title = null;
        this.next = new ArrayList<>();
        this.controller = controller;
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

    public Object getController() {
        return controller;
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
