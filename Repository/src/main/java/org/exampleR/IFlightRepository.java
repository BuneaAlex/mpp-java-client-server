package org.exampleR;

import org.exampleM.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface IFlightRepository extends IRepository<Integer, Flight>{

    List<Flight> findFlightByDestinationAndDatetime(String destination, LocalDateTime date);
    List<Flight> findAvailableFlights();
}
