module com.example.gestionpastients {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.apache.pdfbox;
    opens data to javafx.base;

    opens com.example.ges to javafx.fxml;
    exports com.example.ges;
}