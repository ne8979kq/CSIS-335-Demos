package com.transitions;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReportsController {

    @FXML private Button backBtn, generateBtn, exportBtn;
    @FXML private ProgressBar progress;
    @FXML private Label status;
    @FXML private BarChart<String, Number> chart;

    private Timeline progressTl;

    @FXML
    private void initialize() {
        // Back to main scene
        backBtn.setOnAction(e -> goBack());

        // Fake report generation with progress + chart refresh
        generateBtn.setOnAction(e -> generateReport());

        // Stub export
        exportBtn.setOnAction(e -> {
            status.setText("CSV exported.");
            flashStatus();
        });

        // Seed chart data
        refreshChart(12, 5, 3);
    }

    private void goBack() {
        try {
            BorderPane root = FXMLLoader.load(getClass().getResource("/com/transitions/demo.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Scene scene = new Scene(root, backBtn.getScene().getWidth(), backBtn.getScene().getHeight());
            scene.getStylesheets().add(getClass().getResource("/com/transitions/demo.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generateReport() {
        generateBtn.setDisable(true);
        exportBtn.setDisable(true);
        status.setText("Generating…");
        progress.setProgress(0);

        if (progressTl != null) progressTl.stop();

        progressTl = new Timeline(
                new KeyFrame(Duration.ZERO,          new KeyValue(progress.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(1.0),  new KeyValue(progress.progressProperty(), 0.35)),
                new KeyFrame(Duration.seconds(2.0),  new KeyValue(progress.progressProperty(), 0.7)),
                new KeyFrame(Duration.seconds(2.6),  new KeyValue(progress.progressProperty(), 1.0))
        );
        progressTl.setOnFinished(ev -> {
            status.setText("Report ready.");
            flashStatus();
            // New random-ish numbers to show change
            int ok = (int)(8 + Math.random()*8);
            int pending = (int)(2 + Math.random()*6);
            int failed = (int)(1 + Math.random()*4);
            refreshChart(ok, pending, failed);

            generateBtn.setDisable(false);
            exportBtn.setDisable(false);
        });
        progressTl.play();
    }

    private void refreshChart(int ok, int pending, int failed) {
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Count");
        s.getData().add(new XYChart.Data<>("Completed", ok));
        s.getData().add(new XYChart.Data<>("Pending", pending));
        s.getData().add(new XYChart.Data<>("Failed", failed));
        chart.getData().setAll(s);
    }

    private void flashStatus() {
        FadeTransition f = new FadeTransition(Duration.seconds(0.25), status);
        f.setFromValue(1.0); f.setToValue(0.4);
        f.setAutoReverse(true); f.setCycleCount(2);
        f.play();
    }
}
