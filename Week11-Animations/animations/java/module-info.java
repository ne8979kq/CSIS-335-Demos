module com.transitions {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.transitions to javafx.fxml;
    opens com.transitions.model to javafx.fxml;
    exports com.transitions;
}