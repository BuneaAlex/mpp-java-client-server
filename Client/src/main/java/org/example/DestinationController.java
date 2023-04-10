package org.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.exampleM.Agent;
import org.exampleM.Client;
import org.exampleM.Flight;
import org.exampleM.utils.Constants;
import org.exampleR.service.ChatException;
import org.exampleR.service.IChatObserver;
import org.exampleR.service.IChatServices;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DestinationController implements  IChatObserver {

    public TableView<Flight> allDestinationsTableView;
    public TableColumn<Flight,String> destinationColumn;
    public TableColumn<Flight,String> dateColumn;
    public TableColumn<Flight,String> airportColumn;
    public TableColumn<Flight,String> seatsAvailableColumn;
    public TableView<Flight> destinationsTableView;
    public TableColumn<Flight,String> timeColumn;
    public TableColumn<Flight,String> seatsSearchColumn;
    public TextField destinationTextField;
    public DatePicker datePicker;
    public TextField clientTextField;
    public TextField touristsTextField;
    public TextField addressTextField;
    public TextField noSeatsTextField;
    public Label infoLabel;
    public Button confirmBookingButton;

    private Flight flight = null;

    private IChatServices server;

    @FXML
    private ObservableList<Flight> flights = FXCollections.observableArrayList();

    @FXML
    private ObservableList<Flight> flightsFiltered = FXCollections.observableArrayList();

    private Agent agent;
    private String destinationSearch = "";
    private LocalDateTime dateSearch = LocalDateTime.now();

    public DestinationController(Agent agent,IChatServices server) {
        this.agent = agent;
        this.server = server;
    }

    public DestinationController(IChatServices server) {
        this.server = server;
    }

    public DestinationController()
    {

    }


    public void init() {
        destinationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDestination()));
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().format(Constants.DATE_FORMAT)));
        airportColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAirportName()));
        seatsAvailableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getNoOfSeatsLeft())));

        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().format(Constants.TIME_FORMATTER)));
        seatsSearchColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getNoOfSeatsLeft())));
        allDestinationsTableView.setItems(flights);
        destinationsTableView.setItems(flightsFiltered);
        try {
            initModelFlights();
        } catch (ChatException e) {
            throw new RuntimeException(e);
        }
        confirmBookingButton.setDisable(true);
    }

    void initModelFlights() throws ChatException {
        List<Flight> serviceFlights = server.getAllAvailableFlights();
        flights.setAll(serviceFlights);
        flightsFiltered.clear();
    }

    void initModelFilteredFlights() throws ChatException {
        List<Flight> serviceFilteredFlights = server.getFilteredFlights(destinationSearch,dateSearch);
        flightsFiltered.setAll(serviceFilteredFlights);
        //serviceFilteredFlights.forEach(System.out::println);
    }

    @Override
    public void updateFlights() {
        Platform.runLater(()-> {
            try {
                System.out.println("Updating flights");
                initModelFlights();
                System.out.println("Updated flights");
            } catch (ChatException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void searchDestination(ActionEvent actionEvent) throws ChatException {
        String destination = destinationTextField.getText();
        LocalDateTime datePicked = datePicker.getValue().atStartOfDay();
        //datepicker cond
        if(!Objects.equals(destination, ""))
        {
            destinationSearch = destination;
            dateSearch = datePicked;
            initModelFilteredFlights();
        }
    }

    public void buyTicket(ActionEvent actionEvent) throws IOException {

        int index = destinationsTableView.getSelectionModel().getSelectedIndex();
        if(index!=-1)
        {
            flight = destinationsTableView.getSelectionModel().getSelectedItem();

            if(flight.getNoOfSeatsLeft() > 0)
            {
                infoLabel.setText(flight.getDestination() + ": departure date " + flight.getDate().format(Constants.DATE_FORMAT) + " " +
                        "airport: " + flight.getAirportName());
                confirmBookingButton.setDisable(false);
            }
            else
            {
                SimpleAlertBuilder alert = new SimpleAlertBuilder(Alert.AlertType.WARNING, "Message", "No seats left!");
                alert.show();
            }


        }
    }

    void logout() {
        try {
            server.logout(agent, this);
        } catch (ChatException e) {
            System.out.println("Logout error " + e);
        }
    }


    public void logoutAgent(ActionEvent actionEvent) throws IOException {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setServer(IChatServices server) {
        this.server = server;
    }

    public void bookFlight(ActionEvent actionEvent) throws ChatException {
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

                server.finishBooking(clientToBeAdded,flight);

            }


        }
    }
}

