package org.exampleN.objectprotocol;

import org.exampleM.Agent;
import org.exampleM.Client;
import org.exampleM.Flight;
import org.exampleR.service.ChatException;
import org.exampleR.service.IChatObserver;
import org.exampleR.service.IChatServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ChatServicesObjectProxy implements IChatServices {
    private String host;
    private int port;

    private IChatObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ChatServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        //responses=new ArrayList<Response>();
        qresponses=new LinkedBlockingQueue<Response>();
    }
    @Override
    public void login(Agent agent, IChatObserver client) throws ChatException {
        System.out.println("Init login connection");
        initializeConnection();
        sendRequest(new LoginRequest(agent));
        Response response=readResponse();
        if (response instanceof OkResponse){
            this.client=client;
            return;
        }
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ChatException(err.getMessage());
        }
    }

    @Override
    public void logout(Agent agent, IChatObserver client) throws ChatException {
        System.out.println("logout (proxy)");
        sendRequest(new LogoutRequest(agent));
        Response response=readResponse();
        closeConnection();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ChatException(err.getMessage());
        }
    }
    @Override
    public void finishBooking(Client client, Flight flight) throws ChatException {

        sendRequest(new AddBookingRequest(flight,client));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ChatException(err.getMessage());
        }
    }

    @Override
    public List<Flight> getFilteredFlights(String destination, LocalDateTime date) throws ChatException {

        sendRequest(new GetFlightsRequest(destination,date));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ChatException(err.getMessage());
        }
        GetFlightsResponse resp=(GetFlightsResponse) response;
        List<Flight> flights = resp.getFlightList();

        return flights;
    }

    @Override
    public List<Flight> getAllAvailableFlights() throws ChatException {
        sendRequest(new GetAllAvailableFlightsRequest());
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ChatException(err.getMessage());
        }
        GetAllAvailableFlightsResponse resp=(GetAllAvailableFlightsResponse) response;
        List<Flight> flights = resp.getFlights();

        return flights;
    }

    private void closeConnection() {
        System.out.println("Connection closed");
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws ChatException {
        System.out.println("Sending request" + request);
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ChatException("Error sending object "+e);
        }
    }

    private Response readResponse() throws ChatException {
        System.out.println("Reading response");
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws ChatException {
        System.out.println("Connection open");
         try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        System.out.println("Thread start read!");
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update){
        if (update instanceof FinishBookingResponse){

            FinishBookingResponse upd=(FinishBookingResponse)update;

            System.out.println("Flight booked");
            try {
                client.updateFlights();
            } catch (ChatException e) {
                e.printStackTrace();
            }
        }
    }



    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                         handleUpdate((UpdateResponse)response);
                    }else{
                        /*responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }*/
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
