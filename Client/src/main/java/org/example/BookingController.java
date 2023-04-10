package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.exampleM.Agent;
import org.exampleM.Client;
import org.exampleM.Flight;
import org.exampleM.utils.Constants;
import org.exampleR.service.ChatException;
import org.exampleR.service.IChatServices;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookingController implements Initializable {
    public TextField clientTextField;
    public TextField touristsTextField;
    public TextField addressTextField;
    public TextField noSeatsTextField;
    public Label infoLabel;

    private Flight flight;
    private Agent agent;

    private IChatServices server;

    private DestinationController destinationController;

    public BookingController(Flight flight, Agent agent, IChatServices server, DestinationController destinationController) {
        this.flight = flight;
        this.agent = agent;
        this.server = server;
        this.destinationController = destinationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoLabel.setText(flight.getDestination() + ": departure date " + flight.getDate().format(Constants.DATE_FORMAT) + " " +
                "airport: " + flight.getAirportName());
    }

    public void bookFlight(ActionEvent actionEvent) throws IOException, ChatException {
        String clientName = clientTextField.getText();
        String tourists = touristsTextField.getText();
        String address = addressTextField.getText();
        String noSeats = noSeatsTextField.getText();

        if(!Objects.equals(clientName, "") && !Objects.equals(tourists, "") && !Objects.equals(address, "") && !Objects.equals(noSeats, ""))
        {
            int numberOfSeats = Integer.parseInt(noSeats);
            if(numberOfSeats > flight.getNoOfSeatsLeft())
            {
                SimpleAlertBuilder alert = new SimpleAlertBuilder(Alert.AlertType.WARNING, "Message", "Not enough seats left!");
                alert.show();
            }
            else
            {
                Client clientToBeAdded = new Client(clientName,tourists,address,numberOfSeats);
                flight.setNoOfSeatsLeft(flight.getNoOfSeatsLeft()-numberOfSeats);

//                Client rezClient = service.addClient(clientToBeAdded);
//                service.updateNoSeatsLeftFlight(flight);
//                service.addBooking(rezClient,flight);
                server.finishBooking(clientToBeAdded,flight);

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("destination-view.fxml"));

                fxmlLoader.setControllerFactory(controllerClass -> destinationController);
                Parent root = fxmlLoader.load();

                Scene scene = new Scene(root,800,500);
                Node node = (Node) actionEvent.getSource();
                Stage stage = (Stage) node.getScene().getWindow();

                String title = "Agent " + agent.getId().toString();
                stage.setTitle(title);
                stage.setScene(scene);

            }


        }
    }


}
