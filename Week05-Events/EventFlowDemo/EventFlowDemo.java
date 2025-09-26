import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MixedEventFlowDemoClean extends Application {

    // Helper: is the original mouse target inside (a descendant of) the given node?
    private static boolean isInside(Node target, Node ancestor) {
        for (Node n = target; n != null; n = n.getParent()) {
            if (n == ancestor) return true;
        }
        return false;
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(
                new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Button btn = new Button("Click (ActionEvent)");

        // ===== ACTION EVENT PATH (Button only) =====
        root.addEventFilter(ActionEvent.ACTION, e ->
                System.out.println("ACTION  → FILTER: VBox (capturing)"));

        btn.setOnAction(e ->
                System.out.println("ACTION  → HANDLER: Button (target)"));

        root.addEventHandler(ActionEvent.ACTION, e ->
                System.out.println("ACTION  → HANDLER: VBox (bubbling)"));

        // ===== MOUSE EVENT PATH (Background only) =====
        // Skip logging if the original target is inside the Button
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() instanceof Node && isInside((Node) e.getTarget(), btn)) return;
            System.out.println("MOUSE   → FILTER: VBox (capturing)");
        });

        root.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() instanceof Node && isInside((Node) e.getTarget(), btn)) return;
            System.out.println("MOUSE   → HANDLER: VBox (bubbling)");
        });

        root.getChildren().addAll(btn);
        stage.setScene(new Scene(root, 400, 240));
        stage.setTitle("Button Action vs. VBox Mouse — Clean Split");
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
