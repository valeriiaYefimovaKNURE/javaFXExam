module org.example.kpp_exam {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires eu.hansolo.tilesfx;

    opens org.example.kpp_exam to javafx.fxml;
    exports org.example.kpp_exam;
}