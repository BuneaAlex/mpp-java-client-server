package org.exampleN.objectprotocol;

import org.exampleM.Flight;

import java.util.List;

public class GetFlightsResponse implements Response{

    List<Flight> flightList;

    public GetFlightsResponse(List<Flight> flightList) {
        this.flightList = flightList;
    }

    public List<Flight> getFlightList() {
        return flightList;
    }
}
