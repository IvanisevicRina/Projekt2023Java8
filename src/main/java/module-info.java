module com.example.projekt2023java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    opens com.example.projekt2023java to javafx.fxml;
    opens entitet to javafx.base;
    exports com.example.projekt2023java;
}