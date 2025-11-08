package com.example.animations;

import com.example.animations.model.TaskModel;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.math.RoundingMode;
import java.util.Random;

public class DemoController {
    @FXML private Label statusLabel, toast;
    @FXML private Pane sparkleLayer;
    @FXML private Arc spinnerArc;
    @FXML private StackPane spinner;
    @FXML private Button startBtn, sparkleBtn, errorBtn;
    @FXML private ImageView ggGif;

    private final TaskModel model = new TaskModel();
    private RotateTransition spinnerRT;
    private Timeline trueGlow;

    @FXML private void initialize() {
        // Bind view to model
        statusLabel.textProperty().bind(model.messageProperty());

        // Spinner rotation
        spinnerRT = new RotateTransition(Duration.seconds(1), spinnerArc);
        spinnerRT.setByAngle(360);
        spinnerRT.setCycleCount(Animation.INDEFINITE);
        spinnerRT.setInterpolator(Interpolator.LINEAR);

        // Real glow pulse (Glow chained to DropShadow)
        initGlowPulse();

        // Micro-interaction: magnetic hover on Start
        startBtn.setOnMouseEntered(e -> scaleTo(startBtn, 1.06));
        startBtn.setOnMouseExited(e -> scaleTo(startBtn, 1.0));
    }

    /* ---------- Actions ---------- */

    @FXML private void handleStart() {
        setLoading(true, "Loading…");
        setButtonsDisabled(true);

        var pause = new PauseTransition(Duration.seconds(2.0));
        pause.setOnFinished(e -> {
            model.setStatus(TaskModel.Status.SUCCESS);
            model.setMessage("Done!");
            setLoading(false, null);
            showToast("Success ✓");
            showGG();
            confettiRain(36);
            spawnSparkles(12, sparkleLayer.getWidth()/2, sparkleLayer.getHeight()/2);
            setButtonsDisabled(false);
        });
        pause.play();
    }

    @FXML private void handleError() {
        setLoading(true, "Submitting…");
        setButtonsDisabled(true);

        var pause = new PauseTransition(Duration.seconds(1.2));
        pause.setOnFinished(e -> {
            model.setStatus(TaskModel.Status.ERROR);
            model.setMessage("Error: Network timeout");
            setLoading(false, null);
            showToast("Error ✖");
            shake(statusLabel);
            setButtonsDisabled(false);
        });
        pause.play();
    }

    @FXML private void handleSparkle() {
        spawnSparkles(18, sparkleLayer.getWidth()/2, sparkleLayer.getHeight()/2);
    }

    /* ---------- Visual helpers ---------- */

    private void initGlowPulse() {
        startBtn.setEffect(haloEffect());
        // Top of the chain is Glow
        Glow glow = (Glow) startBtn.getEffect();
        trueGlow = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.08)),
                new KeyFrame(Duration.seconds(0.9), new KeyValue(glow.levelProperty(), 0.95))
        );
        trueGlow.setAutoReverse(true);
        trueGlow.setCycleCount(Animation.INDEFINITE);
        trueGlow.play();
    }

    private Effect haloEffect() {
        var ds = new DropShadow(18, Color.web("#1769e0", 0.65));
        ds.setSpread(0.35);
        var glow = new Glow(0.10);
        glow.setInput(ds); // Glow -> DropShadow chain
        return glow;
    }

    private void setLoading(boolean on, String msg) {
        spinner.setVisible(on);
        if (on) {
            if (msg != null) model.setMessage(msg);
            spinnerRT.play();
        } else {
            spinnerRT.stop();
        }
    }

    private void showToast(String text) {
        toast.setText(text);
        toast.setVisible(true);
        var in = new FadeTransition(Duration.millis(160), toast); in.setToValue(1.0);
        var hold = new PauseTransition(Duration.seconds(1.1));
        var out = new FadeTransition(Duration.millis(260), toast); out.setToValue(0.0);
        out.setOnFinished(e -> toast.setVisible(false));
        new SequentialTransition(in, hold, out).play();
    }

    private void showGG() {
        if (ggGif.getImage() == null) {
            ggGif.setImage(new Image(getClass()
                    .getResource("/com/example/animations/success.gif").toExternalForm(), true));
        }
        ggGif.setOpacity(0);
        ggGif.setVisible(true);
        var in = new FadeTransition(Duration.millis(140), ggGif); in.setToValue(1);
        var hold = new PauseTransition(Duration.seconds(1.2));
        var out = new FadeTransition(Duration.millis(220), ggGif); out.setToValue(0);
        out.setOnFinished(e -> ggGif.setVisible(false));
        new SequentialTransition(in, hold, out).play();
    }

    private void spawnSparkles(int count, double centerX, double centerY) {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            Circle c = new Circle(2 + r.nextDouble() * 2);
            c.setManaged(false);
            c.setLayoutX(centerX);
            c.setLayoutY(centerY);
            c.getStyleClass().add("sparkle");
            sparkleLayer.getChildren().add(c);

            double angle = r.nextDouble() * 360;
            double dist = 40 + r.nextDouble() * 80;
            double dx = Math.cos(Math.toRadians(angle)) * dist;
            double dy = Math.sin(Math.toRadians(angle)) * dist;

            var move = new TranslateTransition(Duration.millis(600 + r.nextInt(400)), c);
            move.setByX(dx); move.setByY(dy);
            move.setInterpolator(Interpolator.EASE_OUT);

            var fade = new FadeTransition(Duration.millis(500 + r.nextInt(300)), c);
            fade.setFromValue(1); fade.setToValue(0);

            var scale = new ScaleTransition(Duration.millis(500), c);
            scale.setFromX(1); scale.setFromY(1);
            scale.setToX(0.5); scale.setToY(0.5);

            var seq = new ParallelTransition(move, fade, scale);
            seq.setOnFinished(e -> sparkleLayer.getChildren().remove(c));
            seq.play();
        }
    }

    private void confettiRain(int n) {
        var r = new Random();
        for (int i = 0; i < n; i++) {
            var piece = new Rectangle(6, 10);
            piece.setManaged(false);
            piece.setFill(Color.hsb(r.nextDouble()*360, 0.85, 1.0));
            piece.setLayoutX(r.nextDouble() * sparkleLayer.getWidth());
            piece.setLayoutY(-20);
            piece.getTransforms().add(new Rotate(0, Rotate.Z_AXIS));
            sparkleLayer.getChildren().add(piece);

            double fall = sparkleLayer.getHeight() + 40 + r.nextDouble()*120;
            double drift = (r.nextDouble()-0.5) * 160;

            var fallAnim = new TranslateTransition(Duration.seconds(1.6 + r.nextDouble()*0.6), piece);
            fallAnim.setByY(fall); fallAnim.setByX(drift);
            fallAnim.setInterpolator(Interpolator.EASE_IN);

            var spin = new RotateTransition(Duration.seconds(0.5 + r.nextDouble()*0.6), piece);
            spin.setByAngle(360 * (r.nextBoolean() ? 1 : -1));
            spin.setCycleCount(Animation.INDEFINITE);

            fallAnim.setOnFinished(e -> { spin.stop(); sparkleLayer.getChildren().remove(piece); });
            spin.play(); fallAnim.play();
        }
    }

    private void shake(javafx.scene.Node node) {
        var tl = new Timeline(
                new KeyFrame(Duration.millis(0),   new KeyValue(node.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(60),  new KeyValue(node.translateXProperty(), -6)),
                new KeyFrame(Duration.millis(120), new KeyValue(node.translateXProperty(), 6)),
                new KeyFrame(Duration.millis(180), new KeyValue(node.translateXProperty(), -4)),
                new KeyFrame(Duration.millis(240), new KeyValue(node.translateXProperty(), 4)),
                new KeyFrame(Duration.millis(300), new KeyValue(node.translateXProperty(), 0))
        );
        tl.play();
    }

    private void scaleTo(javafx.scene.Node node, double s) {
        var st = new ScaleTransition(Duration.millis(120), node);
        st.setToX(s); st.setToY(s); st.play();
    }

    private void setButtonsDisabled(boolean b) {
        startBtn.setDisable(b);
        sparkleBtn.setDisable(b);
        errorBtn.setDisable(b);
    }
}
