package com.itmo.nxzage.common.util.net;

import java.io.Serializable;
import java.util.UUID;
import com.itmo.nxzage.common.util.data.DataContainer;

public class PacketWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID interactionID;
    private PacketType type;
    private DataContainer payload;

    public PacketWrapper(PacketType type, DataContainer payload) {
        this.type = type;
        this.payload = payload;
    }

    public UUID getInteractionID() {
        return interactionID;
    }
    public void setInteractionID(UUID interactionID) {
        this.interactionID = interactionID;
    }

    public void generateInteractionID() {
        this.interactionID = UUID.randomUUID();
    }
    
    public PacketType getType() {
        return type;
    }
    public void setType(PacketType type) {
        this.type = type;
    }
    public DataContainer getPayload() {
        return payload;
    }
    public void setPayload(DataContainer payload) {
        this.payload = payload;
    }

}
