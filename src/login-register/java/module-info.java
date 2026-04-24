module org.example.proiectpip2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;


    opens org.example.proiectpip2 to javafx.fxml;
    exports org.example.proiectpip2;
}