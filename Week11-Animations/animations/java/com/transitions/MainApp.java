package com.transitions;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("demo.fxml"));
        Scene scene = new Scene(fxml.load(), 720, 420);
        stage.setTitle("Week 11 - Animations");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}