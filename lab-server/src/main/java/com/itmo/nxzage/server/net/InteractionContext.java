package com.itmo.nxzage.server.net;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.itmo.nxzage.common.util.net.PacketWrapper;

public class InteractionContext {
    private final UUID id;
    private final InetSocketAddress clientAddress;
    private final PacketWrapper request;
    private final List<PacketWrapper> responses;

    {
        responses = new ArrayList<PacketWrapper>();
    }

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

    public List<PacketWrapper> getResponses() {
        return responses;
    }

    public void addResponse(PacketWrapper response) {
        // TODO: validation?
        response.setInteractionID(id);
        responses.add(response);
    }

    public void setResponses(List<PacketWrapper> responses) {
        if (responses.stream().anyMatch(response -> !(response.getInteractionID().equals(id)))) {
            throw new IllegalArgumentException(
                    "Responses contains packet with wrong interactionID");
        }
        responses.forEach(response -> this.responses.add(response));
    }
}
