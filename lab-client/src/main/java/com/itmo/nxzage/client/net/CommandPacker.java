package com.itmo.nxzage.client.net;

import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.common.util.net.PacketWrapper;

public class CommandPacker {
    private CommandPacker() {
        throw new IllegalStateException("This class is not supposed to be initialized");   
    }

    public static PacketWrapper pack(Command command) {
        var payload = new DataContainer()
            .put("name", command.getName());
        command.getArgs().forEach((arg, value) -> payload.put(arg, value));
        var packet = new PacketWrapper(PacketType.REQUEST, payload);
        packet.generateInteractionID();
        return packet;
    }
}
