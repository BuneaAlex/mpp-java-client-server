package org.exampleN.objectprotocol;


import org.exampleM.Agent;

public class LogoutRequest implements Request {
    private Agent user;

    public LogoutRequest(Agent user) {
        this.user = user;
    }

    public Agent getUser() {
        return user;
    }
}
