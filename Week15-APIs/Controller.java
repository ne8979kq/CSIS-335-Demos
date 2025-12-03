package com.example.api;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

// JSON library (put on classpath)
import org.json.JSONArray;
import org.json.JSONObject;

public class HelloController {

    @FXML
    private ListView<String> listView;

    @FXML
    private Label statusLabel;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    // We'll store the last JSON string so we can save it
    private String lastJson;

    @FXML
    private void initialize() {
        statusLabel.setText("Ready.");
    }

    @FXML
    private void onLoadFromApi() {
        // TODO: 
    }

    @FXML
    private void onSaveCache() {
        // TODO: 
    }

    @FXML
    private void onLoadCache() {
        // TODO: 
    }

    // Helper to turn JSON → ListView items
    private void handleJsonResponse(String json) {
        // TODO: 
    }
}
