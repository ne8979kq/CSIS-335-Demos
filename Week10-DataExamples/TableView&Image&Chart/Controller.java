package com.demo;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
// 🔵 NEW imports
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.collections.ListChangeListener;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller {

    @FXML private TableView<Person> table;
    @FXML private TableColumn<Person, String> nameCol;
    @FXML private TableColumn<Person, String> cityCol;

    @FXML private TextField nameField;
    @FXML private TextField cityField;

    @FXML private Button addBtn;
    @FXML private Button removeBtn;

    @FXML private Label selectedLabel;
    // 🔵 NEW: chart node from FXML
    @FXML private BarChart<String, Number> barChart;

    private final ObservableList<Person> data = FXCollections.observableArrayList(
            new Person("Ronni", "Moorhead", "/images/ss.png"),
            new Person("John", "Fargo", "/images/ps.png")
    );

    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        cityCol.setCellValueFactory(c -> c.getValue().cityProperty());

        table.setItems(data);
        // 🔵 NEW: Image (Pic) column with a cell factory
        TableColumn<Person, String> picCol = new TableColumn<>("Pic");
        picCol.setPrefWidth(64);
        picCol.setCellValueFactory(c -> c.getValue().imageUrlProperty());
        picCol.setCellFactory(col -> new TableCell<>() {
            private final ImageView iv = new ImageView();
            {
                iv.setFitWidth(64); iv.setFitHeight(64); iv.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);
                if (empty || url == null || url.isBlank()) {
                    setGraphic(null);
                } else {
                    try {
                        Image img = url.startsWith("/")
                                ? new Image(getClass().getResourceAsStream(url), 64, 64, true, true)
                                : new Image(url, 32, 32, true, true);
                        iv.setImage(img);
                        setGraphic(iv);
                    } catch (Exception ex) {
                        setGraphic(null); // fail quietly on bad URLs
                    }
                }
            }
        });
        // put the image as the first column
        table.getColumns().add(0, picCol);

        removeBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        selectedLabel.textProperty().bind(
                Bindings.when(table.getSelectionModel().selectedItemProperty().isNull())
                        .then("Selected: (none)")
                        .otherwise(
                                Bindings.concat("Selected: ",
                                        Bindings.selectString(table.getSelectionModel().selectedItemProperty(), "name"))
                        )
        );
        // 🔵 NEW: initial chart + live updates when the list changes
        refreshChart();
        table.getItems().addListener((ListChangeListener<Person>) c -> refreshChart());
    }

    @FXML
    private void handleAdd() {
        String n = nameField.getText().trim();
        String c = cityField.getText().trim();
        if (!n.isEmpty() && !c.isEmpty()) {
            data.add(new Person(n, c));
            nameField.clear();
            cityField.clear();
            table.getSelectionModel().selectLast();
        }
    }

    @FXML
    private void handleRemove() {
        Person sel = table.getSelectionModel().getSelectedItem();
        if (sel != null) data.remove(sel);
    }

    // 🔵 NEW: rebuild chart series from current table data (counts by City)
    private void refreshChart() {
        if (barChart == null) return; // safety if FXML isn't wired
        Map<String, Long> counts = table.getItems().stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("By City");
        series.getData().setAll(
                counts.entrySet().stream()
                        .map(e -> new XYChart.Data<String, Number>(e.getKey(), e.getValue()))
                        .toList()
        );
        barChart.getData().setAll(series);
    }
}