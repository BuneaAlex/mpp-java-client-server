package org.exampleN.objectprotocol;

import org.exampleM.Flight;

import java.util.List;

public class GetAllAvailableFlightsResponse implements Response{

    List<Flight> flights;

    public GetAllAvailableFlightsResponse(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Flight> getFlights() {
        return flights;
    }
}
