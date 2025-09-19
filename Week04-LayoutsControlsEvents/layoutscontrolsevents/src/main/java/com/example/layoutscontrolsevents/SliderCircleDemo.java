package com.example.layoutscontrolsevents;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class SliderCircleDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Circle circle = new Circle(50, Color.CORNFLOWERBLUE);
        Slider slider = new Slider(10, 100, 50);

        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                circle.setRadius(newVal.doubleValue())
        );

        VBox vbox = new VBox(20, slider, circle);
        Scene scene = new Scene(vbox, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Slider Demo");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}