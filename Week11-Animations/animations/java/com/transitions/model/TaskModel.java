package com.transitions.model;

import javafx.beans.property.*;

public class TaskModel {
    private final StringProperty status = new SimpleStringProperty("Idle");
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    public StringProperty statusProperty() { return status; }
    public BooleanProperty loadingProperty() { return loading; }

    public String getStatus() { return status.get(); }
    public void setStatus(String s) { status.set(s); }
    public boolean isLoading() { return loading.get(); }
    public void setLoading(boolean b) { loading.set(b); }
}