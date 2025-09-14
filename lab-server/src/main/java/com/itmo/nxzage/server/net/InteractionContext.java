package com.itmo.nxzage.server.net;

import java.net.InetSocketAddress;
import java.util.UUID;
import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.server.responses.ExecutionResponse;

public class InteractionContext {
    private final UUID id;
    private final InetSocketAddress clientAddress = null; // !UNUSED
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

    // TODO rename
    public Packet getResponses() {
        return response;
    }

    public void setResponse(Packet response) {
        this.response = response;
    }

    public boolean complited() {
        return response != null;
    }
}
