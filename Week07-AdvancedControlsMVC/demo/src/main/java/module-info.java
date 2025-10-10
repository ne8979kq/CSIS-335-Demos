module edu.msum.csis335.demo {
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.web;

        opens edu.msum.csis335.demo to javafx.fxml;
        exports edu.msum.csis335.demo;
        exports edu.msum.csis335.demo.model;
}
