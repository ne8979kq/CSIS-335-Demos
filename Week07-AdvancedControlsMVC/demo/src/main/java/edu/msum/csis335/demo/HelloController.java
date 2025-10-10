package edu.msum.csis335.demo;

import edu.msum.csis335.demo.model.Item;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HelloController {
    @FXML private TextField titleField, urlField;
    @FXML private ListView<Item> listView;
    @FXML private WebView webView;
    @FXML private Label summaryLabel;

    private final ObservableList<Item> items = FXCollections.observableArrayList();
    private WebEngine engine;

    @FXML
    private void initialize() {
        engine = webView.getEngine();

        // Starter items
        items.addAll(
                new Item("OpenJFX", "https://openjfx.io"),
                new Item("MSUM", "https://www.mnstate.edu"),
                new Item("Notes", "")
        );

        // C-level: controls working together (SplitPane + ListView)
        listView.setItems(items);

        // Right-click menu: Rename / Remove
        listView.setCellFactory(lv -> {
            ListCell<Item> cell = new ListCell<>() {
                @Override protected void updateItem(Item it, boolean empty) {
                    super.updateItem(it, empty);
                    setText(empty || it == null ? null : it.getTitle());
                }
            };
            MenuItem rename = new MenuItem("Rename…");
            rename.setOnAction(e -> {
                Item it = cell.getItem();
                if (it == null) return;
                TextInputDialog d = new TextInputDialog(it.getTitle());
                d.setTitle("Rename"); d.setHeaderText(null);
                d.setContentText("New title:");
                d.showAndWait().ifPresent(it::setTitle);
                listView.refresh();
            });
            MenuItem remove = new MenuItem("Remove");
            remove.setOnAction(e -> items.remove(cell.getItem()));
            ContextMenu menu = new ContextMenu(rename, remove);
            cell.emptyProperty().addListener((o, was, isEmpty) -> cell.setContextMenu(isEmpty ? null : menu));
            return cell;
        });

        // Selection → preview (Controller mediates)
        listView.getSelectionModel().selectedItemProperty().addListener((o, old, sel) -> show(sel));

        // Simple dynamic summary (auto-updates when selection changes)
        summaryLabel.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    Item s = listView.getSelectionModel().getSelectedItem();
                    if (s == null) return "No item selected.";
                    String url = s.getUrl();
                    return (url == null || url.isBlank())
                            ? "Selected: " + s.getTitle() + " (no URL — showing generated preview)"
                            : "Selected: " + s.getTitle() + " → " + url;
                },
                listView.getSelectionModel().selectedItemProperty()
        ));

        if (!items.isEmpty()) listView.getSelectionModel().selectFirst();
    }

    @FXML
    private void onAdd() {
        String t = (titleField.getText() == null) ? "" : titleField.getText().trim();
        String u = (urlField.getText()   == null) ? "" : urlField.getText().trim();
        if (t.isBlank()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Please enter a title.", ButtonType.OK);
            a.setHeaderText(null); a.setTitle("Info"); a.showAndWait();
            return;
        }
        items.add(new Item(t, u));
        titleField.clear(); urlField.clear();
        listView.getSelectionModel().selectLast();
    }

    private void show(Item it) {
        if (it == null) {
            engine.loadContent("<html><body style='font-family:system-ui'>No selection.</body></html>");
            return;
        }
        String url = (it.getUrl() == null) ? "" : it.getUrl().trim().toLowerCase();
        if (url.startsWith("http://") || url.startsWith("https://")) {
            engine.load(it.getUrl());
        } else {
            String html = """
                <html><body style='font-family:system-ui;margin:1rem;line-height:1.5'>
                  <h2>%s</h2>
                  <p>This is a simple generated preview because this item has no URL.</p>
                  <p>Tip: right-click an item to Rename or Remove.</p>
                </body></html>
                """.formatted(escape(it.getTitle()));
            engine.loadContent(html);
        }
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
