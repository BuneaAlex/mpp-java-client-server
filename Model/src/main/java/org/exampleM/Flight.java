package org.exampleM;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight implements Identifiable<Integer>{

    private Integer id;

    private String destination;

    private LocalDateTime date;

    private String airportName;

    private int noOfSeatsLeft;

    public Flight(String destination, LocalDateTime date, String airportName, int noOfSeatsLeft) {

        this.destination = destination;
        this.date = date;
        this.airportName = airportName;
        this.noOfSeatsLeft = noOfSeatsLeft;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public int getNoOfSeatsLeft() {
        return noOfSeatsLeft;
    }

    public void setNoOfSeatsLeft(int noOfSeatsLeft) {
        this.noOfSeatsLeft = noOfSeatsLeft;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return noOfSeatsLeft == flight.noOfSeatsLeft && Objects.equals(id, flight.id) && Objects.equals(destination, flight.destination) && Objects.equals(date, flight.date) && Objects.equals(airportName, flight.airportName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, destination, date, airportName, noOfSeatsLeft);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", date=" + date +
                ", airportName='" + airportName + '\'' +
                ", noOfSeatsLeft=" + noOfSeatsLeft +
                '}';
    }
}
