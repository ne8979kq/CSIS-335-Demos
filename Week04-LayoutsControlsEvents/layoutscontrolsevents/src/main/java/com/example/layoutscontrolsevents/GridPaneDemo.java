package com.example.layoutscontrolsevents;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label userLbl = new Label("Username:");
        TextField userFld = new TextField();
        Label passLbl = new Label("Password:");
        PasswordField passFld = new PasswordField();
        Button loginBtn = new Button("Login");
        Label msgLbl = new Label();

        grid.add(userLbl, 0, 0);
        grid.add(userFld, 1, 0);
        grid.add(passLbl, 0, 1);
        grid.add(passFld, 1, 1);
        grid.add(loginBtn, 1, 2);
        grid.add(msgLbl, 1, 3);

        // Event
        loginBtn.setOnAction(e ->
                msgLbl.setText("Welcome, " + userFld.getText())
        );

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GridPane Demo");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
