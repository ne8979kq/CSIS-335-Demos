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
        statusLabel.setText("Calling API...");

        String apiUrl = "https://jsonplaceholder.typicode.com/posts";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    lastJson = json;
                    handleJsonResponse(json);
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    Platform.runLater(() ->
                            statusLabel.setText("Error: " + ex.getMessage())
                    );
                    return null;
                });
    }


    @FXML
    private void onSaveCache() {
        if (lastJson == null) {
            statusLabel.setText("Nothing to cache yet. Load from API first.");
            return;
        }

        try {
            Files.writeString(
                    Path.of("cache.json"),
                    lastJson,
                    StandardCharsets.UTF_8
            );
            statusLabel.setText("Saved JSON to cache.json");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error saving cache: " + e.getMessage());
        }
    }

    @FXML
    private void onLoadCache() {
        try {
            String cached = Files.readString(
                    Path.of("cache.json"),
                    StandardCharsets.UTF_8
            );
            lastJson = cached;
            handleJsonResponse(cached);
            statusLabel.setText("Loaded data from cache.json");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("No cache yet or error reading cache.");
        }
    }

    // Helper to turn JSON → ListView items
    private void handleJsonResponse(String json) {
        // This API returns an array of post objects:
        // [ { "userId": 1, "id": 1, "title": "...", "body": "..." }, ... ]

        JSONArray array = new JSONArray(json);

        ObservableList<String> items = FXCollections.observableArrayList();

        int limit = Math.min(array.length(), 20); // don't spam entire list

        for (int i = 0; i < limit; i++) {
            JSONObject obj = array.getJSONObject(i);
            int id = obj.getInt("id");
            String title = obj.getString("title");
            items.add("#" + id + " – " + title);
        }

        Platform.runLater(() -> {
            listView.setItems(items);
            statusLabel.setText("Loaded " + items.size() + " posts from API.");
        });
    }
}
