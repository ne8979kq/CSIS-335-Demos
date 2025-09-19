package com.example.layoutscontrolsevents;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TabPaneDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Tab tab1 = new Tab("General");
        tab1.setContent(new VBox(new Label("General info here")));

        Tab tab2 = new Tab("Address");
        tab2.setContent(new VBox(new Label("Address info here")));

        TabPane tabPane = new TabPane(tab1, tab2);

        Scene scene = new Scene(tabPane, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TabPane Demo");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}