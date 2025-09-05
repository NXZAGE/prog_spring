package com.itmo.nxzage.server.net;

import java.net.InetSocketAddress;
import java.util.UUID;
import com.itmo.nxzage.common.util.net.PacketWrapper;
import com.itmo.nxzage.server.responses.ExecutionResponse;

public class InteractionContext {
    private final UUID id;
    private final InetSocketAddress clientAddress;
    private final PacketWrapper request;
    private ExecutionResponse response;

    public InteractionContext(PacketWrapper request, InetSocketAddress address) {
        this.id = request.getInteractionID();
        this.request = request;
        this.clientAddress = address;
    }

    public UUID getID() {
        return id;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    public PacketWrapper getRequest() {
        return request;
    }

    public ExecutionResponse getResponses() {
        return response;
    }

    public void setResponse(ExecutionResponse response) {
        this.response = response;
    }

    public boolean complited() {
        return response != null;
    }
}
