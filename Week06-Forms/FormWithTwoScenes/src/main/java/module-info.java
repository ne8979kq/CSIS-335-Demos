module com.example.formwithtwoscenes {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.formwithtwoscenes to javafx.fxml;
    exports com.example.formwithtwoscenes;
}