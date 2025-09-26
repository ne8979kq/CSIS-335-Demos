import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class MainController {
    private final TextField name; private final Button greet; private final Label out;
    private final Circle ball; private final Scene scene; private final Stage stage;

    public MainController(TextField name, Button greet, Label out, Circle ball, Scene scene, Stage stage) {
        this.name = name; this.greet = greet; this.out = out; this.ball = ball; this.scene = scene; this.stage = stage;
    }

    public void wire() {
        // ACTION
        greet.setOnAction(e -> handleGreet());
        name.setOnAction(e -> greet.fire());

        // MOUSE
        ball.setOnMouseClicked(e -> {
            ball.setFill(ball.getFill().equals(Color.CORAL) ? Color.CORNFLOWERBLUE : Color.CORAL);
            // Example consume toggle:
            // e.consume();
        });

        // KEY
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) { name.clear(); out.setText("Cleared"); }
        });

        // SCENE click
        scene.setOnMouseClicked(e -> System.out.println("Scene HANDLER (" + e.getSceneX() + ", " + e.getSceneY() + ")"));
    }
    private void handleGreet() { out.setText("Hi, " + name.getText() + "!"); }
}