module org.example.proiectpip2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires java.net.http;
    requires java.sql;
    requires org.slf4j;

    opens org.example.proiectpip2 to javafx.fxml;

    exports org.example.proiectpip2;
    exports org.example.proiectpip2.infra;
    exports fileUploader;
    exports fileUploader.account;
    exports fileUploader.model;
    exports fileUploader.organizr;
    exports fileUploader.ui;
}
