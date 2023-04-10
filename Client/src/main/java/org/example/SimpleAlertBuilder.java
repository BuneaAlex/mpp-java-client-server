package org.example;

import javafx.scene.control.Alert;

public class SimpleAlertBuilder {

    private Alert alert;
    private Alert.AlertType type;
    private String title;
    private String message;

    public SimpleAlertBuilder(Alert.AlertType type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;

        alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
    }

    public void show()
    {
        alert.show();
    }
}
