package com.transitions;

import com.transitions.model.TaskModel;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class DemoController {
    // View
    @FXML private Button loadBtn, successBtn, errorBtn, cta, reportsBtn;
    @FXML private Label title, statusLabel, bannerText, bannerIcon;
    @FXML private VBox card;
    @FXML private HBox banner;
    @FXML private Circle spinner;
    @FXML private StackPane rootStack;

    // Model
    private final TaskModel model = new TaskModel();

    // Animations
    private RotateTransition spinnerRotate;
    private Timeline dotsTimeline;              // "Loading." -> ".." -> "..."
    private SequentialTransition bannerSeq;     // Slide-in/out banner

    private int queue = 5; // fake items for "Process Next"

    @FXML
    private void initialize() {
        // Bind model -> view
        statusLabel.textProperty().bind(model.statusProperty());

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45);
        Lighting lighting = new Lighting(light);
        title.setOnMouseEntered(e -> title.setEffect(lighting));
        title.setOnMouseExited(e -> title.setEffect(null));

        spinner.setMouseTransparent(true);
        spinnerRotate = new RotateTransition(Duration.seconds(1), spinner);
        spinnerRotate.setByAngle(360);
        spinnerRotate.setCycleCount(Animation.INDEFINITE);
        spinnerRotate.setInterpolator(Interpolator.LINEAR);

        dotsTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,         ae -> model.setStatus("Loading.")),
                new KeyFrame(Duration.seconds(0.4), ae -> model.setStatus("Loading..")),
                new KeyFrame(Duration.seconds(0.8), ae -> model.setStatus("Loading..."))
        );
        dotsTimeline.setCycleCount(Animation.INDEFINITE);
        dotsTimeline.getCuePoints().put("midway", Duration.seconds(0.4));

        banner.setOpacity(0);
        banner.setTranslateY(-50);
        banner.setMouseTransparent(true);
        banner.setManaged(false);
        bannerSeq = buildBannerSequence("#22543d", "✓", "Ready.");

        loadBtn.setOnAction(e -> simulateLoadThenSuccess("Loading data…", "Loaded."));
        successBtn.setOnAction(e -> quickSave());
        errorBtn.setOnAction(e -> showErrorShake());
        cta.setOnAction(e -> processNext());
        reportsBtn.setOnAction(e -> goReports());

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.2), cta);
        scale.setAutoReverse(true);
        scale.setCycleCount(Animation.INDEFINITE);
        scale.setFromX(1.0); scale.setFromY(1.0);
        scale.setToX(1.05);  scale.setToY(1.05);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), cta);
        fade.setAutoReverse(true);
        fade.setCycleCount(Animation.INDEFINITE);
        fade.setFromValue(1.0); fade.setToValue(0.85);

        new ParallelTransition(scale, fade).play();

        updateTitle();
    }

    // =====================
    // Distinct actions
    // =====================

    private void goReports() {
        try {
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/com/transitions/reports.fxml"));
            var stage = (javafx.stage.Stage) rootStack.getScene().getWindow();
            var scene = new javafx.scene.Scene(root, rootStack.getScene().getWidth(), rootStack.getScene().getHeight());
            // keep your shared stylesheet
            scene.getStylesheets().add(getClass().getResource("/com/transitions/demo.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void simulateLoadThenSuccess(String loadingMsg, String doneMsg) {
        setBusy(true);

        spinner.setOpacity(1);
        spinnerRotate.playFromStart();
        dotsTimeline.playFrom("midway");
        model.setLoading(true);

        bannerSeq.stop();
        bannerSeq = buildBannerSequence("#1f2937", "⟳", loadingMsg);
        bannerSeq.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(2.0));
        pause.setOnFinished(e -> {
            showSuccess(doneMsg);
            setBusy(false);
        });
        pause.play();
    }

    private void quickSave() {
        model.setStatus("Saved manually.");
        greenGlow();
        showSuccess("Saved.");
    }

    private void processNext() {
        if (queue <= 0) {
            model.setStatus("Nothing to process.");
            bannerSeq.stop();
            bannerSeq = buildBannerSequence("#92400e", "!", "No more items.");
            bannerSeq.play();
            wobble(card);
            return;
        }

        int ordinal = (6 - queue);
        model.setStatus("Processing item #" + ordinal + "…");
        simulateLoadThenSuccess("Processing item #" + ordinal, "Item #" + ordinal + " done.");
        queue--;
        updateTitle();
    }

    // =====================
    // Feedback variants
    // =====================

    private void showSuccess(String message) {
        model.setLoading(false);
        dotsTimeline.stop();
        model.setStatus("Success!");

        spinnerRotate.stop();
        spinner.setOpacity(0);

        Timeline celebrate = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(card.scaleXProperty(), 1.0, Interpolator.EASE_BOTH),
                        new KeyValue(card.scaleYProperty(), 1.0, Interpolator.EASE_BOTH),
                        new KeyValue(card.rotateProperty(),  0.0, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(0.15),
                        new KeyValue(card.scaleXProperty(), 1.06),
                        new KeyValue(card.scaleYProperty(), 1.06)
                ),
                new KeyFrame(Duration.seconds(0.35),
                        new KeyValue(card.scaleXProperty(), 1.0),
                        new KeyValue(card.scaleYProperty(), 1.0),
                        new KeyValue(card.rotateProperty(),  1.5)
                ),
                new KeyFrame(Duration.seconds(0.6),
                        new KeyValue(card.rotateProperty(),  0.0)
                )
        );
        celebrate.play();

        bannerSeq.stop();
        bannerSeq = buildBannerSequence("#166534", "✓", message);
        bannerSeq.play();
    }

    private void showErrorShake() {
        dotsTimeline.stop();
        spinnerRotate.stop();
        spinner.setOpacity(0);
        model.setStatus("Error — try again.");

        Timeline shake = new Timeline(
                new KeyFrame(Duration.ZERO,        new KeyValue(card.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(60),  new KeyValue(card.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(120), new KeyValue(card.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(180), new KeyValue(card.translateXProperty(), -8)),
                new KeyFrame(Duration.millis(240), new KeyValue(card.translateXProperty(), 8)),
                new KeyFrame(Duration.millis(300), new KeyValue(card.translateXProperty(), 0))
        );
        shake.play();

        bannerSeq.stop();
        bannerSeq = buildBannerSequence("#7f1d1d", "!", "Something went wrong.");
        bannerSeq.play();
    }

    // =====================
    // Visual helpers
    // =====================

    private SequentialTransition buildBannerSequence(String bgColor, String icon, String text) {
        banner.setStyle("-fx-background-color: " + bgColor + "; -fx-padding:8; -fx-background-radius: 0 0 6 6;");
        bannerIcon.setText(icon);
        bannerText.setText(text);

        TranslateTransition down = new TranslateTransition(Duration.millis(220), banner);
        down.setFromY(-50); down.setToY(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(180), banner);
        fadeIn.setFromValue(0); fadeIn.setToValue(1);

        ParallelTransition show = new ParallelTransition(down, fadeIn);
        show.setOnFinished(e -> { banner.setMouseTransparent(false); banner.setManaged(true); });

        PauseTransition hold = new PauseTransition(Duration.seconds(1.2));

        TranslateTransition up = new TranslateTransition(Duration.millis(220), banner);
        up.setFromY(0); up.setToY(-50);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), banner);
        fadeOut.setFromValue(1); fadeOut.setToValue(0);

        ParallelTransition hide = new ParallelTransition(up, fadeOut);
        hide.setOnFinished(e -> { banner.setMouseTransparent(true); banner.setManaged(false); });

        return new SequentialTransition(show, hold, hide);
    }

    private void greenGlow() {
        DropShadow ds = new DropShadow();
        ds.setRadius(18);
        ds.setSpread(0.25);
        ds.setColor(Color.web("#22c55e"));
        card.setEffect(ds);

        FadeTransition f = new FadeTransition(Duration.seconds(0.35), card);
        f.setFromValue(1.0); f.setToValue(0.95);
        f.setAutoReverse(true); f.setCycleCount(2);
        f.setOnFinished(ev -> card.setEffect(null));
        f.play();
    }

    private void wobble(VBox node) {
        Timeline wobble = new Timeline(
                new KeyFrame(Duration.ZERO,        new KeyValue(node.rotateProperty(), 0)),
                new KeyFrame(Duration.millis(80),  new KeyValue(node.rotateProperty(), -3)),
                new KeyFrame(Duration.millis(160), new KeyValue(node.rotateProperty(), 3)),
                new KeyFrame(Duration.millis(240), new KeyValue(node.rotateProperty(), 0))
        );
        wobble.play();
    }

    private void setBusy(boolean busy) {
        loadBtn.setDisable(busy);
        successBtn.setDisable(busy);
        errorBtn.setDisable(busy);
        cta.setDisable(busy);
    }

    private void updateTitle() {
        title.setText("Orders (" + queue + " left)");
    }
}
