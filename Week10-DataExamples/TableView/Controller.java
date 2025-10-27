package com.example.demo;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    @FXML private TableView<Person> table;
    @FXML private TableColumn<Person, String> nameCol;
    @FXML private TableColumn<Person, String> cityCol;

    @FXML private TextField nameField;
    @FXML private TextField cityField;

    @FXML private Button addBtn;
    @FXML private Button removeBtn;

    @FXML private Label selectedLabel;

    private final ObservableList<Person> data = FXCollections.observableArrayList(
            new Person("Ronni", "Moorhead"),
            new Person("John", "Fargo"),
            new Person("Jane", "Duluth")
    );

    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        cityCol.setCellValueFactory(c -> c.getValue().cityProperty());

        table.setItems(data);

        removeBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        selectedLabel.textProperty().bind(
                Bindings.when(table.getSelectionModel().selectedItemProperty().isNull())
                        .then("Selected: (none)")
                        .otherwise(
                                Bindings.concat("Selected: ",
                                        Bindings.selectString(table.getSelectionModel().selectedItemProperty(), "name"))
                        )
        );
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
}
