package org.example;

import org.exampleN.objectprotocol.utils.AbstractServer;
import org.exampleN.objectprotocol.utils.ChatObjectConcurrentServer;
import org.exampleN.objectprotocol.utils.ServerException;
import org.exampleR.service.AgencyService;
import org.exampleR.service.IChatServices;

import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {

       Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/chatserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }

        IChatServices chatServerImpl= AgencyService.getInstance();
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new ChatObjectConcurrentServer(chatServerPort, chatServerImpl);
        try {
                server.start();
        } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
