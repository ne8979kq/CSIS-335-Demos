module com.example.layoutscontrolsevents {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.layoutscontrolsevents to javafx.fxml;
    exports com.example.layoutscontrolsevents;
}