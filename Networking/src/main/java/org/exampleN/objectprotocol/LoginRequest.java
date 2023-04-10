package org.exampleN.objectprotocol;


import org.exampleM.Agent;

public class LoginRequest implements Request {
    private Agent user;

    public LoginRequest(Agent user) {
        this.user = user;
    }

    public Agent getUser() {
        return user;
    }
}
