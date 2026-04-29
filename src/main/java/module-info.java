module org.example.proiectpip2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires java.net.http;

    opens org.example.proiectpip2 to javafx.fxml;
    opens welcomePage to javafx.fxml;

    exports org.example.proiectpip2;
    exports fileUploader;
    exports fileUploader.account;
    exports fileUploader.model;
    exports fileUploader.organizr;
    exports fileUploader.ui;
    exports welcomePage;
}
