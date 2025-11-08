package com.example.animations.model;

import javafx.beans.property.*;

public class TaskModel {
    public enum Status { IDLE, LOADING, SUCCESS, ERROR }
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.IDLE);
    private final StringProperty message = new SimpleStringProperty("Ready");

    public ObjectProperty<Status> statusProperty() { return status; }
    public Status getStatus() { return status.get(); }
    public void setStatus(Status s) { status.set(s); }

    public StringProperty messageProperty() { return message; }
    public String getMessage() { return message.get(); }
    public void setMessage(String m) { message.set(m); }
}
