package com.example.eventflowdemo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EventFlowDemo extends Application {
    // The code for this example isn't quite as important as the running program itself.
    // It's a bit overcomplicated in order to demonstrate the way it needs to - and it's because of the way
    // that Buttons automatically consume the event when reached.

    // Helper: is the original event target inside the given ancestor node?
    private static boolean isInside(Node target, Node ancestor) {
        for (Node n = target; n != null; n = n.getParent()) {
            if (n == ancestor) return true;
        }
        return false;
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.setBackground(new Background(
                new BackgroundFill(Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        Button btn = new Button("Click (fires ActionEvent)");
        CheckBox cbConsumeAction = new CheckBox("Consume ActionEvent at Button");
        CheckBox cbConsumeMouseFilter = new CheckBox("Consume MouseEvent at VBox FILTER");

        // ===== ACTION EVENT PATH (Button) =====
        root.addEventFilter(ActionEvent.ACTION, e ->
                System.out.println("ACTION  → FILTER: VBox (capturing)"));

        btn.setOnAction(e -> {
            System.out.println("ACTION  → HANDLER: Button (target)");
            if (cbConsumeAction.isSelected()) {
                System.out.println("ACTION  → Button consumed (no VBox bubbling)");
                e.consume(); // stops ActionEvent from bubbling to VBox
            }
        });

        root.addEventHandler(ActionEvent.ACTION, e ->
                System.out.println("ACTION  → HANDLER: VBox (bubbling)"));

        // ===== MOUSE EVENT PATH (Background clicks only) =====
        // Ignore mouse events whose original target is the button or inside it.
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() instanceof Node && isInside((Node) e.getTarget(), btn)) return;
            System.out.println("MOUSE   → FILTER: VBox (capturing)");
            if (cbConsumeMouseFilter.isSelected()) {
                System.out.println("MOUSE   → VBox filter consumed (no bubbling)");
                e.consume(); // stops further processing (no bubbling handler)
            }
        });

        root.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() instanceof Node && isInside((Node) e.getTarget(), btn)) return;
            System.out.println("MOUSE   → HANDLER: VBox (bubbling)");
        });

        root.getChildren().addAll(btn, cbConsumeAction, cbConsumeMouseFilter);

        stage.setScene(new Scene(root, 460, 240));
        stage.setTitle("Capture → Target → Bubble (with consume toggles)");
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}

