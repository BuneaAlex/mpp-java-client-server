module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires Repository.org.exampleR;
    requires Model.org.exampleM;
    requires Networking.org.exampleN;

    opens org.example to javafx.fxml;
    exports org.example;
}