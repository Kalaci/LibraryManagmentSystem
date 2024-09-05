module com.example.improvedlibrarymanagment {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens Model to javafx.base; // Open the Model package to javafx.base
    opens Views to javafx.fxml; // Open the Views package to javafx.fxml if you use FXML
    opens Controllers to javafx.fxml; // Open the Controllers package to javafx.fxml if you use FXML

    exports Model;
    exports Views;
    exports Controllers;
}