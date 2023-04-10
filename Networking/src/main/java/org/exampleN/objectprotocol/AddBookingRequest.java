package org.exampleN.objectprotocol;

import org.exampleM.Client;
import org.exampleM.Flight;

public class AddBookingRequest implements Request {

    private Flight flight;
    private Client client;

    public AddBookingRequest(Flight flight, Client client) {
        this.flight = flight;
        this.client = client;
    }

    public Flight getFlight() {
        return flight;
    }

    public Client getClient() {
        return client;
    }
}
