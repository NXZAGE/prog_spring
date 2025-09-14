package com.itmo.nxzage.server.services.net;

import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.server.exceptions.InvalidRequestException;

public class RequestFilterService {
    public void filter(Packet packet) {
        if (packet.getInteractionID() == null) {
            throw new InvalidRequestException();
        }
        PacketType type = packet.getType();
        switch (type) {
            case REQUEST -> {
            }
            case REGISTER_REQUEST -> {
            }
            case PING -> {
            }
            default -> throw new InvalidRequestException();
        };
        // TODO доп проверки payload
    }
}
