package com.example.demo;

// We didn't get to this in class due to the brown out, but
// we'll talk about it next time.
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Data Week Demo - TableView + Binding (MVC)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
