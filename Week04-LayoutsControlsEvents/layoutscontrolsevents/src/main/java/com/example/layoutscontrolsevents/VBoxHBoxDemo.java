package com.example.layoutscontrolsevents;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class VBoxHBoxDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label userLbl = new Label("Username:");
        TextField userFld = new TextField();
        Label passLbl = new Label("Password:");
        PasswordField passFld = new PasswordField();

        Button loginBtn = new Button("Login");
        Label msgLbl = new Label();

        // Event
        loginBtn.setOnAction(e ->
                msgLbl.setText("Welcome, " + userFld.getText())
        );

        // VBox for vertical stacking
        VBox vbox = new VBox(10, userLbl, userFld, passLbl, passFld, loginBtn, msgLbl);
        vbox.setPadding(new Insets(10));

        //HBox hbox = new HBox(10, userLbl, userFld, passLbl, passFld, loginBtn, msgLbl);
        //hbox.setPadding(new Insets(10));

        // uncomment the hbox stuff above and change vbox in line 36 to hbox
        // to see how they differ!

        // Scene setup
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("VBox Demo");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}