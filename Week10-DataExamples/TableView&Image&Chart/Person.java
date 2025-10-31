package com.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty city = new SimpleStringProperty();
    // 🔵 NEW: image URL property
    private final SimpleStringProperty imageUrl = new SimpleStringProperty("");

    public Person(String name, String city) {
        this.name.set(name);
        this.city.set(city);
        // 🔵 NEW: optional default image (leave empty if you prefer)
        this.imageUrl.set("");
    }

    // 🔵 NEW: optional 3-arg convenience constructor
    public Person(String name, String city, String imageUrl) {
        this.name.set(name);
        this.city.set(city);
        this.imageUrl.set(imageUrl == null ? "" : imageUrl);
    }

    public String getName() { return name.get(); }
    public void setName(String n) { name.set(n); }
    public StringProperty nameProperty() { return name; }

    public String getCity() { return city.get(); }
    public void setCity(String c) { city.set(c); }
    public StringProperty cityProperty() { return city; }

    // 🔵 NEW: image accessors
    public String getImageUrl() { return imageUrl.get(); }
    public void setImageUrl(String url) { imageUrl.set(url); }
    public SimpleStringProperty imageUrlProperty() { return imageUrl; }
}