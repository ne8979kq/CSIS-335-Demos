package com.example.animations;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/animations/view.fxml"));
        stage.setTitle("Motion with Meaning — MVC Demo");
        stage.setScene(new Scene(root, 680, 460));
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}
