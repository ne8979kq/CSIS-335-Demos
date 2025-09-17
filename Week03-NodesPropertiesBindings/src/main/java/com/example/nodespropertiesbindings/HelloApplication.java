package com.example.nodespropertiesbindings;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Label label = new Label("Hello JavaFX!");

        //Nodes & Bounds (& Buttons!)
        Button b = new Button("Click Me");
        b.setEffect(new DropShadow());
        System.out.println("layoutBounds: " + b.getLayoutBounds());
        System.out.println("boundsInLocal: " + b.getBoundsInLocal());

        //Properties
        IntegerProperty counter = new SimpleIntegerProperty(1);
        counter.addListener((obs, oldVal, newVal) ->
                System.out.println("Changed: " + oldVal + " → " + newVal));
        counter.set(2);
        counter.set(5);

        //Bindings (unidirectional)
        IntegerProperty x = new SimpleIntegerProperty(10);
        IntegerProperty y = new SimpleIntegerProperty(20);
        IntegerProperty z = new SimpleIntegerProperty();
        z.bind(x.add(y));
        System.out.println(z.get()); // 30
        x.set(15);
        System.out.println(z.get()); // 35

        //Slider -> Label
        Slider slider = new Slider(8, 48, 16);
        Label slideLabel = new Label("Resizable text");
        slideLabel.styleProperty().bind(
                slider.valueProperty().asString("-fx-font-size: %.0fpx;")
        );

        //bidirectional binding
        TextField t1 = new TextField("Hello");
        TextField t2 = new TextField();
        t1.textProperty().bindBidirectional(t2.textProperty());

        //now we'll put everything in a VBox
        VBox root = new VBox(10, label, b, slider, slideLabel,
                new HBox(8, t1, t2));

        stage.setTitle("Nodes, Properties, Bindings");
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}