package org.exampleR.service;


import org.exampleM.Agent;
import org.exampleM.Client;
import org.exampleM.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface IChatServices {
     void login(Agent agent, IChatObserver client) throws ChatException;
     void logout(Agent agent, IChatObserver client) throws ChatException;

     void finishBooking(Client client,Flight flight) throws ChatException;

     List<Flight> getFilteredFlights(String destination, LocalDateTime date) throws ChatException;
     List<Flight> getAllAvailableFlights() throws ChatException;
}
