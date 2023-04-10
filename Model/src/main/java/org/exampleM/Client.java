package org.exampleM;

import java.util.Objects;

public class Client implements Identifiable<Integer>{

    private Integer id;

    private String name;

    private String touristNames;
    private String address;
    private int noOfSeatsReserved;

    public Client(String name, String touristNames, String address, int noOfSeatsReserved) {

        this.name = name;
        this.touristNames = touristNames;
        this.address = address;
        this.noOfSeatsReserved = noOfSeatsReserved;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTouristNames() {
        return touristNames;
    }

    public void setTouristNames(String touristNames) {
        this.touristNames = touristNames;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNoOfSeatsReserved() {
        return noOfSeatsReserved;
    }

    public void setNoOfSeatsReserved(int noOfSeatsReserved) {
        this.noOfSeatsReserved = noOfSeatsReserved;
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
        Client client = (Client) o;
        return noOfSeatsReserved == client.noOfSeatsReserved && Objects.equals(id, client.id) && Objects.equals(name, client.name) && Objects.equals(touristNames, client.touristNames) && Objects.equals(address, client.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, touristNames, address, noOfSeatsReserved);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", touristNames='" + touristNames + '\'' +
                ", address='" + address + '\'' +
                ", noOfSeatsReserved=" + noOfSeatsReserved +
                '}';
    }
}
