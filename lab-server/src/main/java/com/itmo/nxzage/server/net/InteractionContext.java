package com.itmo.nxzage.server.net;

import java.util.UUID;
import com.itmo.nxzage.common.util.net.Packet;

public class InteractionContext {
    private final UUID id;
    private final Packet request;
    private Packet response;

    public InteractionContext(Packet request) {
        this.id = request.getInteractionID();
        this.request = request;
        // this.clientAddress = address;
    }

    public UUID getID() {
        return id;
    }

    // public InetSocketAddress getClientAddress() {
    //     return clientAddress;
    // }

    public Packet getRequest() {
        return request;
    }

    public Packet getResponse() {
        return response;
    }

    public void setResponse(Packet response) {
        this.response = response;
    }

    public boolean complited() {
        return response != null;
    }
}
