package org.exampleN.objectprotocol;


import java.time.LocalDateTime;

public class GetFlightsRequest implements Request {

    private String destination;
    private LocalDateTime date;

    public GetFlightsRequest(String destination, LocalDateTime date) {
        this.destination = destination;
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDate() {
        return date;
    }
}