package org.example;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.exampleR.service.AgencyService;


public abstract class AbstractController {

    protected AgencyService service = AgencyService.getInstance();

    protected void closeWindow(ActionEvent actionEvent)
    {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
