package com.example.persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class HelloController {

    @FXML private TableView<Score> table;
    @FXML private TableColumn<Score, String> nameCol;
    @FXML private TableColumn<Score, Number> scoreCol;

    private final ObservableList<Score> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        scoreCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getScore()));
        table.setItems(data);
    }

    @FXML
    private void onLoadJson() {
        try (FileReader reader = new FileReader("src/data.json")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Score>>() {}.getType();
            List<Score> list = gson.fromJson(reader, listType);

            data.setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
