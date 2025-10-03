package com.example.formwithtwoscenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Label;


import java.io.IOException;


public class ConfirmController {


    @FXML private Label summaryLabel;
    public void setSummaryText(String text) {
        summaryLabel.setText(text);
    }


    @FXML
    private void handleBack() throws IOException {
        // Return to the form view
        Parent formRoot = FXMLLoader.load(getClass().getResource("formView.fxml"));
        Node any = summaryLabel; // any node on current scene
        any.getScene().setRoot(formRoot);
    }
}
