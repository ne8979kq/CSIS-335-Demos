import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        TextField name = new TextField(); name.setPromptText("Your name");
        Button greet = new Button("Greet");
        Label out = new Label("…");
        Circle ball = new Circle(50, Color.CORAL);

        VBox root = new VBox(10, new Label("Name:"), name, greet, out, ball);
        Scene scene = new Scene(root, 360, 240);

        // Hand the nodes to the controller and let it wire events:
        MainController c = new MainController(name, greet, out, ball, scene, stage);
        c.wire();

        stage.setScene(scene);
        stage.setTitle("Controller Mini-Demo");
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}