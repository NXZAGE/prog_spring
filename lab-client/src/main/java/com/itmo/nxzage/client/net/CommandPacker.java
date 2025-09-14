package com.itmo.nxzage.client.net;

import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.common.util.net.Packet;

public class CommandPacker {
    private CommandPacker() {
        throw new IllegalStateException("This class is not supposed to be initialized");   
    }

    public static Packet pack(Command command) {
        var payload = new DataContainer()
            .put("name", command.getName());
        command.getArgs().forEach((arg, value) -> payload.put(arg, value));
        PacketType packetType = command.getName().equals("register") ? PacketType.REGISTER_REQUEST : PacketType.REQUEST;
        var packet = new Packet(packetType, payload);
        packet.generateInteractionID();
        return packet;
    }
}
