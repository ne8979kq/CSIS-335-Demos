package com.example.formwithtwoscenes;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;


import java.io.IOException;
import java.util.function.UnaryOperator;


public class FormController {


    @FXML private TextField nameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> typeChoice;
    @FXML private Button submitBtn;
    @FXML private Label errorLabel;


    @FXML
    private void initialize() {
        // Choice values
        typeChoice.getItems().setAll("Student", "Faculty", "Guest");


        // Numeric-only formatter for age (up to 3 digits)
        ageField.setTextFormatter(numericFormatter());


        // Basic validation: require name, age, type; password length >= 6 (example)
        BooleanBinding invalid = nameField.textProperty().isEmpty()
                .or(typeChoice.valueProperty().isNull())
                .or(ageField.textProperty().isEmpty())
                .or(passwordField.textProperty().length().lessThan(6));


        submitBtn.disableProperty().bind(invalid);
        errorLabel.visibleProperty().bind(invalid);
        errorLabel.setText("Fill all fields; password ≥ 6 chars; age numeric.");
    }


    @FXML
    private void handleSubmit() throws IOException {
        // Gather sanitized values
        String name = nameField.getText().trim();
        String role = typeChoice.getValue();
        String ageTxt = ageField.getText().trim();


        // Lightweight extra validation guard (should be disabled already)
        if (name.isEmpty() || role == null || ageTxt.isEmpty()) return;


        int age = Integer.parseInt(ageTxt);


        // Build confirmation summary
        String summary = String.format("Name: %s%nRole: %s%nAge: %d", name, role, age);


        // Load confirm view and inject summary via controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmView.fxml"));
        Parent confirmRoot = loader.load();
        ConfirmController confirm = loader.getController();
        confirm.setSummaryText(summary);


        // Swap the root on the existing Scene (keeps size/styles)
        Node any = submitBtn; // any node on current scene
        any.getScene().setRoot(confirmRoot);
    }


    // ----- Helpers -----
    private static TextFormatter<Integer> numericFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();
            return text.matches("\\d{0,3}") ? change : null; // up to 3 digits
        };
        return new TextFormatter<>(new IntegerStringConverter(), null, filter);
    }
}
