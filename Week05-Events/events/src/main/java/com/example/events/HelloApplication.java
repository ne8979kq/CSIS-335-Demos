package com.example.events;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        TextField name = new TextField();
        name.setPromptText("Your name");
        Button greet = new Button("Greet");
        Label out = new Label("…");
        Circle ball = new Circle(50, Color.CORAL);
        VBox root = new VBox(10,
                new HBox(8, new Label("Name:"), name),
                greet, out, ball
        );
        Scene scene = new Scene(root, 360, 240);

        // ACTION: button click; TextField Enter triggers same behavior
        greet.setOnAction(e -> out.setText("Hi, " + name.getText() + "!"));
        name.setOnAction(e -> greet.fire()); // Enter maps to button click

        // KEYBOARD: low-level (pressed) vs high-level (typed)
        scene.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.Z) {
                out.setText("Undo (Ctrl+Z)");
                e.consume(); // stop bubbling
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                name.clear();
                out.setText("Cleared");
            }
        });
        name.setOnKeyTyped(e -> System.out.println("Typed: '" + e.getCharacter() + "'"));

        // MOUSE: click the ball; Shift-click consumes (parents won't see it)
        ball.setOnMouseClicked(e -> {
            out.setText("Ball clicked (" + e.getButton() + ")");
            if (e.isShiftDown()) e.consume();
        });

        // Scene-wide mouse click (only fires if not consumed below)
        scene.setOnMouseClicked(e ->
                System.out.println("Scene saw a click at (" + e.getSceneX() + ", " + e.getSceneY() + ")")
        );
        stage.setScene(scene);
        stage.setTitle("setOnXxx Demo");
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}
