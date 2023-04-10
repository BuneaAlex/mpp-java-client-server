package org.exampleN.objectprotocol;

import org.exampleM.Agent;
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


public class ChatClientObjectWorker implements Runnable, IChatObserver {
    private IChatServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ChatClientObjectWorker(IChatServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                   sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }


    private Response handleRequest(Request request){
        Response response=null;
        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            Agent agent=logReq.getUser();

            try {
                server.login(agent, this);
                return new OkResponse();
            } catch (ChatException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest){
            System.out.println("Logout request");
            LogoutRequest logReq=(LogoutRequest)request;
            Agent agent=logReq.getUser();
            try {
                server.logout(agent, this);
                connected=false;
                return new OkResponse();

            } catch (ChatException e) {
               return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof AddBookingRequest){
            System.out.println("addBookingRequest ...");
            AddBookingRequest senReq=(AddBookingRequest)request;

            try {
                server.finishBooking(senReq.getClient(),senReq.getFlight());
                 return new AddBookingResponse();
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof GetFlightsRequest){
            System.out.println("GetFlightsRequest Request ...");
            GetFlightsRequest getReq=(GetFlightsRequest)request;
            LocalDateTime date = getReq.getDate();
            String destination = getReq.getDestination();
            try {
                List<Flight> flights = server.getFilteredFlights(destination,date);
                return new GetFlightsResponse(flights);
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof GetAllAvailableFlightsRequest){
            System.out.println("getAllAvailableFlightsRequest Request ...");
            GetAllAvailableFlightsRequest getReq=(GetAllAvailableFlightsRequest)request;
            try {
                List<Flight> flights = server.getAllAvailableFlights();
                return new GetAllAvailableFlightsResponse(flights);
            } catch (ChatException e) {
                return new ErrorResponse(e.getMessage());
            }
        }


        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
    @Override
    public void updateFlights() throws ChatException {
        try {
            sendResponse(new FinishBookingResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
