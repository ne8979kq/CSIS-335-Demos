module com.example.nodespropertiesbindings {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nodespropertiesbindings to javafx.fxml;
    exports com.example.nodespropertiesbindings;
}