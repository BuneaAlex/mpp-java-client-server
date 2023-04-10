package org.exampleN.objectprotocol.utils;


import org.exampleN.objectprotocol.ChatClientObjectWorker;
import org.exampleR.service.IChatServices;

import java.net.Socket;


public class ChatObjectConcurrentServer extends AbsConcurrentServer {
    private IChatServices chatServer;
    public ChatObjectConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ChatClientObjectWorker worker=new ChatClientObjectWorker(chatServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
