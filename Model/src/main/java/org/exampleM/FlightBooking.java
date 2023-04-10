package org.exampleM;

import org.exampleM.utils.Pair;

public class FlightBooking implements Identifiable<Pair<Integer,Integer>>{

    private Integer id_flight;
    private Integer id_client;

    private Flight flight;
    private Client client;

    public FlightBooking(Flight flight, Client client) {
        this.flight = flight;
        this.client = client;
    }

    @Override
    public Pair<Integer, Integer> getID() {
        return new Pair<>(id_client, id_flight);
    }

    @Override
    public void setID(Pair<Integer, Integer> id) {
        id_client = id.first;
        id_flight = id.second;
    }

    public Flight getFlight() {
        return flight;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "FlightBooking{" +
                "id_flight=" + id_flight +
                ", id_client=" + id_client +
                ", flight=" + flight +
                ", client=" + client +
                '}';
    }
}
