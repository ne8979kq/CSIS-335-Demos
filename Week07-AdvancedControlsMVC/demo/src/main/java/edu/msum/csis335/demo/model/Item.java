package edu.msum.csis335.demo.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty url   = new SimpleStringProperty();

    public Item(String title, String url) {
        this.title.set(title);
        this.url.set(url);
    }

    public StringProperty titleProperty() { return title; }
    public String getTitle() { return title.get(); }
    public void setTitle(String t) { title.set(t); }

    public StringProperty urlProperty() { return url; }
    public String getUrl() { return url.get(); }
    public void setUrl(String u) { url.set(u); }

    @Override public String toString() { return getTitle(); }
}