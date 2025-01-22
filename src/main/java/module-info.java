module org.example.db_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    opens org.example.db_app to javafx.fxml;
    exports org.example.db_app;
}