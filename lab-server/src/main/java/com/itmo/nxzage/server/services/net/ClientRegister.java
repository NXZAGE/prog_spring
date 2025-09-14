package com.itmo.nxzage.server.services.net;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import com.itmo.nxzage.server.logging.ServerLogger;

public final class ClientRegister {
    private final Map<UUID, InetSocketAddress> sockets;
    private final Map<UUID, Date> interactionTimestamp;
    private final Logger logger = ServerLogger.getLogger("ClientRegister");

    {
        sockets = new HashMap<>();
        interactionTimestamp = new HashMap<>();
    }

    public void registerInteraction(UUID id, InetSocketAddress client) {
        if (sockets.get(id) != null && !sockets.get(id).equals(client)) {
            logger.severe("Different clients send the same interactions. Interaction banned");
            // TODO do real ban
            sockets.put(id, null);
            return;
        }

        sockets.put(id, client);
        interactionTimestamp.put(id, new Date());
    }

    public InetSocketAddress getClientAddress(UUID id) {
        if (sockets.get(id) == null) {
            throw new IllegalArgumentException(String.format("No client address for interaction with UUID=%s".formatted(id.toString())));
        }

        return sockets.get(id);
    }
}
