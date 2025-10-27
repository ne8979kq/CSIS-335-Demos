package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty city = new SimpleStringProperty();

    public Person(String name, String city) {
        this.name.set(name);
        this.city.set(city);
    }

    public String getName() { return name.get(); }
    public void setName(String n) { name.set(n); }
    public StringProperty nameProperty() { return name; }

    public String getCity() { return city.get(); }
    public void setCity(String c) { city.set(c); }
    public StringProperty cityProperty() { return city; }
}