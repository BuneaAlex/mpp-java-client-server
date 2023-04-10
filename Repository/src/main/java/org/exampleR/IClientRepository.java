package org.exampleR;

import org.exampleM.Client;

import java.util.List;

public interface IClientRepository extends IRepository<Integer, Client>{

    List<Client> findClientsByNoOfSeatsReserved(int noOfSeats);
}
