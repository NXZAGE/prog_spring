package com.itmo.nxzage.server.services.net;

import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.server.exceptions.InvalidRequestException;
import com.itmo.nxzage.server.net.InteractionContext;

public class RequestFilterService {
    public void filter(InteractionContext context) {
        if (context.getID() == null) {
            throw new InvalidRequestException();
        }
        PacketType type = context.getRequest().getType();
        switch (type) {
            case REQUEST -> {
            }
            case PING -> {
            }
            default -> throw new InvalidRequestException();
        };
        // TODO доп проверки payload
    }
}
