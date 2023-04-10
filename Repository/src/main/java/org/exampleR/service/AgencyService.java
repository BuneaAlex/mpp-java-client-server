package org.exampleR.service;

import org.exampleM.Agent;
import org.exampleM.Client;
import org.exampleM.Flight;
import org.exampleM.FlightBooking;
import org.exampleM.utils.Pair;
import org.exampleR.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgencyService implements IChatServices {

    private final IAgentRepository agentRepository;
    private final IClientRepository clientRepository;
    private final IFlightRepository flightRepository;
    private final IFlightBookingRepository flightBookingRepository;
    static AgencyService instance = new AgencyService(new AgentDBRepository(),new ClientDBRepository(),new FlightDBRepository(),new FlightBookingDBRepository());

    private Map<Integer, IChatObserver> loggedClients;
    private AgencyService(IAgentRepository agentRepository, IClientRepository clientRepository, IFlightRepository flightRepository, IFlightBookingRepository flightBookingRepository) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
        this.flightBookingRepository = flightBookingRepository;
        loggedClients=new ConcurrentHashMap<>();
    }

    public static AgencyService getInstance() {
        return instance;
    }

    @Override
    public synchronized void login(Agent agent, IChatObserver client) throws ChatException {
        Agent agentR = agentRepository.findAgentByLoginInformation(agent.getAgencyName(),agent.getUserName(),agent.getPassword());

        if(agentR != null)
        {
            if(loggedClients.get(agentR.getId())!=null)
                throw new ChatException("User already logged in.");
            loggedClients.put(agentR.getId(), client);
        }
        else
            throw new ChatException("Authentication failed.");
    }

    @Override
    public synchronized void logout(Agent agent, IChatObserver client) throws ChatException {
        IChatObserver localClient=loggedClients.remove(agent.getId());
        if (localClient==null)
            throw new ChatException("User "+agent.getId()+" is not logged in.");
    }

    private final int defaultThreadsNo=5;

    public void bookFlightNotification() {
        Iterable<Agent> agents= agentRepository.findAll();
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);


        for(Agent agent :agents){
            IChatObserver chatClient=loggedClients.get(agent.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying ["+agent.getId()+"] agent ["+agent.getId()+"] made a sale.");
                        chatClient.updateFlights();
                    } catch (ChatException e) {
                        System.out.println("Error notifying agent " + e);
                    }
                });

        }
        executor.shutdown();
    }

    public List<Flight> getAllAvailableFlights()
    {
        return flightRepository.findAvailableFlights();
    }

    public List<Flight> getFilteredFlights(String destination, LocalDateTime date)
    {
        return flightRepository.findFlightByDestinationAndDatetime(destination, date);
    }

    public Agent getLoginAgent(String agencyName,String userName,String password)
    {
        return agentRepository.findAgentByLoginInformation(agencyName,userName,password);
    }

    public Client addClient(Client client)
    {
        clientRepository.save(client);
        int idMax = 0;
        for(Client c : clientRepository.findAll())
        {
            if(c.getID() > idMax)
                idMax = c.getId();
        }

        return clientRepository.findOne(idMax);
    }

    public void addBooking(Client client,Flight flight)
    {
        FlightBooking booking = new FlightBooking(flight,client);
        booking.setID(new Pair<Integer,Integer>(client.getID(),flight.getID()));
        flightBookingRepository.save(booking);
    }
    public void updateNoSeatsLeftFlight(Flight flight)
    {
        flightRepository.update(flight);
    }

    public synchronized void finishBooking(Client client,Flight flight)
    {
        Client rezClient = addClient(client);
        updateNoSeatsLeftFlight(flight);
        addBooking(rezClient,flight);
        bookFlightNotification();
    }

}
