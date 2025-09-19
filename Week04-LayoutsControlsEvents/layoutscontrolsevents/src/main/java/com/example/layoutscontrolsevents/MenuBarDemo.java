package com.example.layoutscontrolsevents;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuBarDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");

        // Event
        exitItem.setOnAction(e -> primaryStage.close());

        fileMenu.getItems().add(exitItem);
        menuBar.getMenus().add(fileMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MenuBar Demo");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
